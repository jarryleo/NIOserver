package cn.leo.business.executor;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.bean.UserBean;
import cn.leo.business.constant.MsgCode;
import cn.leo.business.constant.MsgType;
import cn.leo.business.message.MsgManager;
import cn.leo.business.user.UserManager;
import cn.leo.kotlin.db.UserDao;
import cn.leo.nio.utils.Logger;

import java.nio.channels.SelectionKey;

public class MsgLogin implements MsgExecutor {
    private static MsgExecutor msgExecutor;

    private MsgLogin() {
    }

    public static MsgExecutor getInstance() {
        if (msgExecutor == null) {
            msgExecutor = new MsgLogin();
        }
        return msgExecutor;
    }

    @Override
    public void executeMsg(SelectionKey key, MsgBean msgBean) {
        String name = msgBean.getMsg();
        //数据库查询用户是否存在
        UserDao dao = new UserDao();
        UserBean login = dao.login(key, name);
        //返回登录信息
        MsgBean msg = new MsgBean();
        msg.setType(MsgType.SYS.getType());
        if (login != null) {
            //登录成功
            UserManager.removeUser(key);
            UserManager.addUser(key, login);
            //返回登录成功的消息
            msg.setCode(MsgCode.LOG_SUC.getCode());
            //把用户登录的bean的json发送过去
            msg.setMsg(login.toString());
        } else {
            //登录失败
            msg.setCode(MsgCode.LOG_FAI.getCode());
        }
        MsgManager.sendMsg(key, msg);
    }
}
