package cn.leo.business.message;

import cn.leo.business.bean.MsgBean;
import cn.leo.nio.service.ServiceCore;
import cn.leo.business.bean.UserInfoBean;
import cn.leo.business.user.UserManager;
import cn.leo.nio.utils.Logger;
import cn.leo.nio.utils.TextUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;

public class MsgManager {
    private final static int TIME_OUT = 5000;// 超时时间 5000 毫秒

    /**
     * 发送消息到单一对象
     *
     * @param key
     * @param msg
     */
    public static void sendMsg(SelectionKey key, MsgBean msg) {
        String info = new Gson().toJson(msg);
        ServiceCore.sendMsg(key, info.getBytes());
    }

    /**
     * 群发消息
     *
     * @param msg
     */
    public static void sendMsgToAll(MsgBean msg) {
        Set<SelectionKey> users = UserManager.getUsers();

        Iterator<SelectionKey> iterator = users.iterator();
        long currentTime = System.currentTimeMillis();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            UserInfoBean user = UserManager.getUser(selectionKey);
            if (TextUtil.isEmpty(user.getUserName()) && currentTime - user.getConnectTime() > TIME_OUT) {
                // 超时没有登录的链接剔除
                try {
                    selectionKey.cancel();
                    selectionKey.channel().close();
                    UserManager.removeUser(selectionKey);
                    Logger.i("断开未登录的链接---" + user.getIp());
                } catch (IOException e) {
                }
            } else {
                // 发送消息
                sendMsg(selectionKey, msg);
            }
        }
    }

    /**
     * 处理客户端发来的信息
     *
     * @param key
     * @param msgJson
     */
    public static void processMsg(SelectionKey key, String msgJson) {
        MsgBean msgBean = new Gson().fromJson(msgJson, MsgBean.class);
        switch (msgBean.getType()) {
            case MsgBean.TYPE_SYS:
                break;
        }

    }
}
