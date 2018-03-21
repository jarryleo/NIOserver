package cn.leo.nio.service;

import java.nio.channels.SelectionKey;

import cn.leo.nio.message.MsgHeartbeat;
import cn.leo.nio.message.MsgManager;
import cn.leo.nio.user.UserInfoBean;
import cn.leo.nio.user.UserManager;
import cn.leo.nio.utils.Logger;

public class ServiceManager implements ServiceListener {

	public ServiceManager() {
		ServiceCore.StartService(this); // 启动服务器
		MsgHeartbeat.StartHeartbeat();// 开启心跳检测机制
	}

	// 新连接接入
	@Override
	public void onNewConnectComing(SelectionKey key) {
		UserInfoBean user = new UserInfoBean(key);
		UserManager.addUser(key, user);
		Logger.d("有客户端接入---" + user.getIp());
		int size = UserManager.getmUsers().size();
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
		String msg = new String(data);
		MsgManager.processMsg(key, msg);

		// Logger.d(msg + "-----" + UserManager.getUser(key).getIp());
		// MsgBean msgBean = new MsgBean();
		// msgBean.setMsg(msg);
		// MsgManager.sendMsg(key, msgBean);// 把客服端发来的消息回发到客户端
	}

	// 移除异常连接
	public static synchronized void removeConnect(SelectionKey key) {
		UserInfoBean user = UserManager.getUser(key);
		if (user == null || key == null)
			return;
		Logger.d("有客户端失去连接---" + user.getIp());
		UserManager.removeUser(key);
		int size = UserManager.getmUsers().size();
		Logger.d("clientCount:" + size);

	}
}
