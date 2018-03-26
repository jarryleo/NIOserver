package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;

import java.nio.channels.SelectionKey;

public class MsgPri implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgPri() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgPri();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {

    }
}
