package cn.leo.nio.service;

import cn.leo.business.message.GameControl;
import cn.leo.kotlin.utils.PropertiesUtil;
import cn.leo.business.message.MsgHeartbeat;
import cn.leo.business.message.MsgManager;
import cn.leo.business.bean.UserBean;
import cn.leo.business.user.UserManager;
import cn.leo.nio.utils.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.channels.SelectionKey;

public class ServiceManager implements ServiceListener {

    public ServiceManager() {
        ServiceCore.StartService(this, PropertiesUtil.INSTANCE.getPort()); // 启动服务器
        MsgHeartbeat.startHeartbeat();// 开启心跳检测机制
        GameControl.startGameControl();// 开启游戏流程控制器
    }

    // 新连接接入
    @Override
    public void onNewConnectComing(SelectionKey key) {
        UserBean user = new UserBean(key);
        UserManager.addUser(key, user);
        Logger.d("有客户端接入---" + user.getIp());
        int size = UserManager.getUsers().size();
        Logger.d("clientCount:" + size);
    }

    // 有连接断开
    @Override
    public void onConnectInterrupt(SelectionKey key) {
        removeConnect(key);
    }

    // 消息抵达
    @Override
    public void onDataArrived(SelectionKey key, byte[] data) {
        String msg = null;
        try {
            if (data.length == 0) return;
            String s = new String(data, 0, 1, "utf-8");
            if ("P".equals(s)) {
                MsgManager.processPaint(key, data);
            } else {
                msg = new String(data, "utf-8");
                MsgManager.processMsg(key, msg);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // 移除异常连接
    public static synchronized void removeConnect(SelectionKey key) {
        UserBean user = UserManager.getUser(key);
        if (user == null || key == null)
            return;
        Logger.d("有客户端失去连接---" + user.getIp());
        UserManager.removeUser(key);
        int size = UserManager.getUsers().size();
        Logger.d("clientCount:" + size);

    }
}
