package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.message.MsgManager;
import cn.leo.nio.utils.Logger;

import java.nio.channels.SelectionKey;

public class MsgError implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgError() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgError();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {
        MsgManager.InterceptConnection(key, "错误消息类型");
    }
}
