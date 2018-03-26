package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;

import java.nio.channels.SelectionKey;

public class MsgPaint implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgPaint() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgPaint();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {

    }
}
