package cn.leo.nio.service;

import java.nio.channels.SelectionKey;

public interface ServiceListener {
	/**
	 * 服务端有新的连接介入
	 * 
	 * @param key
	 */
	void onNewConnectComing(SelectionKey key);

	/**
	 * 有连接中断
	 * 
	 * @param key
	 */
	void onConnectInterrupt(SelectionKey key);

	/**
	 * 有数据抵达
	 * 
	 * @param key
	 * @param data
	 */
	void onDataArrived(SelectionKey key, byte[] data);
}
