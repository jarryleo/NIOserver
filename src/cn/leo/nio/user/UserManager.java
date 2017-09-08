package cn.leo.nio.user;

import java.nio.channels.SelectionKey;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

	private static ConcurrentHashMap<SelectionKey, UserInfoBean> mUsers = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<UserInfoBean, SelectionKey> mKeys = new ConcurrentHashMap<>();

	/**
	 * 添加连接到管理类
	 * 
	 * @param user
	 */
	public static void addUser(SelectionKey key, UserInfoBean user) {
		mUsers.put(key, user);
		mKeys.put(user, key);
	}

	/**
	 * 移除连接
	 * 
	 * @param user
	 */
	public static void removeUser(SelectionKey key) {
		UserInfoBean user = mUsers.get(key);
		mUsers.remove(key);
		mKeys.remove(user);
	}

	/**
	 * 获取所有在线连接
	 * 
	 * @return
	 */
	public static Set<SelectionKey> getmUsers() {
		return mUsers.keySet();
	}

	/**
	 * 根据连接获取用户
	 * 
	 * @param key
	 * @return
	 */
	public static UserInfoBean getUser(SelectionKey key) {
		return mUsers.get(key);
	}

	/**
	 * 根据用户获取连接
	 * 
	 * @param user
	 * @return
	 */
	public static SelectionKey getKey(UserInfoBean user) {
		return mKeys.get(user);
	}
}
