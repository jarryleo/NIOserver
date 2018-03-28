package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.RoomBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.constant.MsgType;
import cn.leo.business.message.MsgManager;
import cn.leo.business.room.RoomManager;
import cn.leo.business.user.UserManager;

import java.nio.channels.SelectionKey;

public class RoomCreate implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private RoomCreate() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new RoomCreate();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {
        //创建房间成功
        UserBean user = UserManager.getUser(key);
        RoomBean room = RoomManager.createRoom(user);
        //返回创建成功消息(房间json)
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.GAME.getType());
        msg.setCode(MsgCode.ROOM_CREATE_SUC.getCode());
        msg.setMsg(room.toString());
        MsgManager.sendMsg(key, msg);
    }
}