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

public class RoomExit implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private RoomExit() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new RoomExit();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {
        UserBean user = UserManager.getUser(key);
        RoomBean room = user.getRoom();
        if (room != null) {
            room.removeUser(user);
        }
    }

}
