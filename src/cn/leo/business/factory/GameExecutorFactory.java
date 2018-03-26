package cn.leo.business.factory;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.constant.MsgType;
import cn.leo.business.executor.*;

import java.nio.channels.SelectionKey;

public class GameExecutorFactory {

    public static void executeMsg(SelectionKey key, MsgBean msgBean) {
        getMsgExecutor(msgBean.getCode()).executeMsg(key, msgBean);
    }

    private static MsgExecutor getMsgExecutor(int msgCode) {
        if (msgCode == MsgCode.ROOM_LIST.getCode()) {
            return RoomList.getInstance();
        }
        return MsgError.getInstance();
    }
}
