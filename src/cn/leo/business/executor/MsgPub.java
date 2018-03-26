package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.message.MsgManager;
import cn.leo.business.user.UserManager;

import java.nio.channels.SelectionKey;

public class MsgPub implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgPub() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgPub();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {
        //UserBean user = UserManager.getUser(key);
        MsgManager.sendMsgToAll(msgBean);
    }
}
