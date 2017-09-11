package cn.leo.nio.user;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class UserInfoBean {
	private SelectionKey selectionKey;
	private int userId;
	private String userName;
	private String ip;
	private int sex;
	private long connectTime;

	public UserInfoBean(SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
		this.connectTime = System.currentTimeMillis();
		try {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			InetSocketAddress address = (InetSocketAddress) socketChannel.getRemoteAddress();
			ip = address.getHostString();
		} catch (IOException e) {

		}
	}

	public SelectionKey getSelectionKey() {
		return selectionKey;
	}

	public long getConnectTime() {
		return connectTime;
	}

	public void setSelectionKey(SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public void setConnectTime(long connectTime) {
		this.connectTime = connectTime;
	}
	
}
