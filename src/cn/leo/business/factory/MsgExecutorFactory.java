package cn.leo.business.factory;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.executor.*;
import cn.leo.business.constant.MsgType;

import java.nio.channels.SelectionKey;

public class MsgExecutorFactory {

    public static void executeMsg(SelectionKey key, MsgBean msgBean) {
        getMsgExecutor(msgBean.getType()).executeMsg(key, msgBean);
    }

    private static MsgExecutor getMsgExecutor(MsgType msgType) {
        switch (msgType) {
            case SYS:
                return MsgSystem.getInstance();
            case REG:
                return MsgReg.getInstance();
            case LOGIN:
                return MsgLogin.getInstance();
            case GAME:
                return MsgGame.getInstance();
            case PAINT:
                return MsgPaint.getInstance();
            case PRI:
                return MsgPri.getInstance();
            case PUB:
                return MsgPub.getInstance();
        }
        return MsgError.getInstance();
    }
}
