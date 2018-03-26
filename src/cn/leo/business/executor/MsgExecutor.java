package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;

import java.nio.channels.SelectionKey;

public interface MsgExecutor {
    void executeMsg(SelectionKey key, MsgBean msgBean);
}
