package cn.leo.nio.service;

import java.nio.channels.SelectionKey;

public interface ServiceListener {
	/**
	 * ��������µ����ӽ���
	 * 
	 * @param key
	 */
	void onNewConnectComing(SelectionKey key);

	/**
	 * �������ж�
	 * 
	 * @param key
	 */
	void onConnectInterrupt(SelectionKey key);

	/**
	 * �����ݵִ�
	 * 
	 * @param key
	 * @param data
	 */
	void onDataArrived(SelectionKey key, byte[] data);
}
