package cn.leo.business.user;

import cn.leo.business.bean.UserBean;

import java.nio.channels.SelectionKey;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

    private static ConcurrentHashMap<SelectionKey, UserBean> mUsers = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<UserBean, SelectionKey> mKeys = new ConcurrentHashMap<>();

    /**
     * 添加连接到管理类
     *
     * @param user
     */
    public static void addUser(SelectionKey key, UserBean user) {
        if (mKeys.containsKey(user)) {
            SelectionKey key1 = mKeys.get(user);
            mUsers.remove(key1);
            mKeys.remove(user);
        }
        mUsers.put(key, user);
        mKeys.put(user, key);
    }

    /**
     * 移除连接
     *
     * @param key
     */
    public static void removeUser(SelectionKey key) {
        UserBean user = mUsers.get(key);
        mUsers.remove(key);
        mKeys.remove(user);
    }

    /**
     * 获取所有在线连接
     *
     * @return
     */
    public static Set<SelectionKey> getUsers() {
        return mUsers.keySet();
    }

    /**
     * 根据连接获取用户
     *
     * @param key
     * @return
     */
    public static UserBean getUser(SelectionKey key) {
        return mUsers.get(key);
    }

    /**
     * 根据用户获取连接
     *
     * @param user
     * @return
     */
    public static SelectionKey getKey(UserBean user) {
        return mKeys.get(user);
    }
}
