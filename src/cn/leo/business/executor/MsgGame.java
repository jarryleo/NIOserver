package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.factory.GameExecutorFactory;

import java.nio.channels.SelectionKey;

public class MsgGame implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgGame() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgGame();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {
        GameExecutorFactory.executeMsg(key, msgBean);
    }
}
