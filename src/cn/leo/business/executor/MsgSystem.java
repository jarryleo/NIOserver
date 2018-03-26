package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;

import java.nio.channels.SelectionKey;

public class MsgSystem implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgSystem() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgSystem();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {

    }
}
