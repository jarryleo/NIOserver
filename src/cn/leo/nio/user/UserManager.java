package cn.leo.nio.user;

import java.nio.channels.SelectionKey;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

	private static ConcurrentHashMap<SelectionKey, UserInfoBean> mUsers = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<UserInfoBean, SelectionKey> mKeys = new ConcurrentHashMap<>();

	/**
	 * ������ӵ�������
	 * 
	 * @param user
	 */
	public static void addUser(SelectionKey key, UserInfoBean user) {
		mUsers.put(key, user);
		mKeys.put(user, key);
	}

	/**
	 * �Ƴ�����
	 * 
	 * @param user
	 */
	public static void removeUser(SelectionKey key) {
		UserInfoBean user = mUsers.get(key);
		mUsers.remove(key);
		mKeys.remove(user);
	}

	/**
	 * ��ȡ������������
	 * 
	 * @return
	 */
	public static Set<SelectionKey> getmUsers() {
		return mUsers.keySet();
	}

	/**
	 * �������ӻ�ȡ�û�
	 * 
	 * @param key
	 * @return
	 */
	public static UserInfoBean getUser(SelectionKey key) {
		return mUsers.get(key);
	}

	/**
	 * �����û���ȡ����
	 * 
	 * @param user
	 * @return
	 */
	public static SelectionKey getKey(UserInfoBean user) {
		return mKeys.get(user);
	}
}
