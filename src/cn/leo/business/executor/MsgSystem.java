package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.user.UserManager;

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
        //更新心跳
        UserBean user = UserManager.getUser(key);
        user.refreshHeart(msgBean);
    }
}
