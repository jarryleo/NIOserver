package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;

import java.nio.channels.SelectionKey;

public class MsgReg implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgReg() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgReg();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {

    }
}
