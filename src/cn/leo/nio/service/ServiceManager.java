package cn.leo.nio.service;

import java.nio.channels.SelectionKey;

import cn.leo.nio.message.MsgHeartbeat;
import cn.leo.nio.message.MsgManager;
import cn.leo.nio.user.UserInfoBean;
import cn.leo.nio.user.UserManager;
import cn.leo.nio.utils.Logger;

public class ServiceManager implements ServiceListener {

	public ServiceManager() {
		ServiceCore.StartService(this); // ����������
		MsgHeartbeat.StartHeartbeat();// ��������������
	}

	// �����ӽ���
	@Override
	public void onNewConnectComing(SelectionKey key) {
		UserInfoBean user = new UserInfoBean(key);
		UserManager.addUser(key, user);
		Logger.d("�пͻ��˽���---" + user.getIp());
		int size = UserManager.getmUsers().size();
		Logger.d("clientCount:" + size);
	}

	// �����ӶϿ�
	@Override
	public void onConnectInterrupt(SelectionKey key) {
		removeConnect(key);
	}

	// ��Ϣ�ִ�
	@Override
	public void onDataArrived(SelectionKey key, byte[] data) {
		String msg = new String(data);
		MsgManager.processMsg(key, msg);

		// Logger.d(msg + "-----" + UserManager.getUser(key).getIp());
		// MsgBean msgBean = new MsgBean();
		// msgBean.setMsg(msg);
		// MsgManager.sendMsg(key, msgBean);// �ѿͷ��˷�������Ϣ�ط����ͻ���
	}

	// �Ƴ��쳣����
	public static synchronized void removeConnect(SelectionKey key) {
		UserInfoBean user = UserManager.getUser(key);
		if (user == null || key == null)
			return;
		Logger.d("�пͻ���ʧȥ����---" + user.getIp());
		UserManager.removeUser(key);
		int size = UserManager.getmUsers().size();
		Logger.d("clientCount:" + size);

	}
}
