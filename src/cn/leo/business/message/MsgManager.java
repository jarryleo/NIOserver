package cn.leo.business.message;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.RoomBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.factory.MsgExecutorFactory;
import cn.leo.business.user.UserManager;
import cn.leo.nio.service.ServiceCore;
import cn.leo.nio.utils.GsonUtil;
import cn.leo.nio.utils.Logger;
import cn.leo.nio.utils.SocketUtil;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MsgManager {
    private final static int TIME_OUT = 30_000;// 超时时间  毫秒

    /**
     * 发送消息到单一对象
     *
     * @param key
     * @param msg
     */
    public static void sendMsg(SelectionKey key, MsgBean msg) {
        String info = msg.toString();
        ServiceCore.sendMsg(key, info.getBytes());
    }

    /**
     * 给所有连接服务器的客户端群发消息
     *
     * @param msg
     */
    public static void sendMsgToAll(MsgBean msg) {
        Set<SelectionKey> users = UserManager.getUsers();
        for (SelectionKey selectionKey : users) {
            // 发送消息
            sendMsg(selectionKey, msg);
        }
    }

    /**
     * 发送消息给房间内用户
     *
     * @param userBean     发消息的用户
     * @param msgBean      要转发的消息
     * @param exceptSender 不转发给发消息的用户
     */
    public static void sendMsgToRoom(UserBean userBean, MsgBean msgBean, boolean exceptSender) {
        RoomBean room = userBean.getRoom();
        List<UserBean> users = room.getUsers();
        for (UserBean user : users) {
            //不发给发消息的人
            if (user == userBean && exceptSender) {
                continue;
            }
            sendMsg(userBean.getSelectionKey(), msgBean);
        }
    }

    /**
     * 处理客户端发来的信息
     *
     * @param key
     * @param msgJson
     */
    public static void processMsg(SelectionKey key, String msgJson) {
        MsgBean msgBean = null;
        //判断消息是否合法
        msgBean = GsonUtil.fromJson(msgJson, MsgBean.class);
        if (msgBean == null) {
            InterceptConnection(key, "非法连接");
            return;
        }
        //消息分发策略
        MsgExecutorFactory.executeMsg(key, msgBean);
    }

    public static void checkHeart() {
        Set<SelectionKey> users = UserManager.getUsers();
        Iterator<SelectionKey> iterator = users.iterator();
        long currentTime = System.currentTimeMillis();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            UserBean user = UserManager.getUser(selectionKey);
            if (/*TextUtil.isEmpty(user.getUserName()) &&*/
                    currentTime - user.getConnectTime() > TIME_OUT) {
                // 超时没有登录的链接剔除
                InterceptConnection(selectionKey, "未登录");
            }
        }
    }

    /**
     * 断开指定链接，当有非法连接出现时
     *
     * @param selectionKey
     */
    public static void InterceptConnection(SelectionKey selectionKey, String errorMsg) {
        try {
            Logger.i("断开链接[" + SocketUtil.getSelectionKeyIp(selectionKey) + "] - " + errorMsg);
            selectionKey.cancel();
            selectionKey.channel().close();
            UserManager.removeUser(selectionKey);
        } catch (IOException e) {
            //不处理
        }
    }

}
