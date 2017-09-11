package cn.leo.nio.message;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.leo.nio.service.ServiceCore;
import cn.leo.nio.user.UserInfoBean;
import cn.leo.nio.user.UserManager;
import cn.leo.nio.utils.Logger;
import cn.leo.nio.utils.TextUtil;

public class MsgManager {
	private final static int TIME_OUT = 5000;// 超时时间 5000 毫秒

	/**
	 * 发送消息到单一对象
	 * 
	 * @param key
	 * @param msg
	 */
	public static void sendMsg(SelectionKey key, MsgBean msg) {
		String info = JSON.toJSONString(msg);
		ServiceCore.sendMsg(key, info.getBytes());
	}

	/**
	 * 群发消息
	 * 
	 * @param msg
	 */
	public static void sendMsgToAll(MsgBean msg) {
		Set<SelectionKey> users = UserManager.getmUsers();

		Iterator<SelectionKey> iterator = users.iterator();
		long currentTime = System.currentTimeMillis();
		while (iterator.hasNext()) {
			SelectionKey selectionKey = iterator.next();
			UserInfoBean user = UserManager.getUser(selectionKey);
//			if (TextUtil.isEmpty(user.getUserName()) && currentTime - user.getConnectTime() > TIME_OUT) {
//				// 超时没有登录的链接剔除
//				try {
//					selectionKey.cancel();
//					selectionKey.channel().close();
//					UserManager.removeUser(selectionKey);
//					Logger.i("断开未登录的链接---" + user.getIp());
//				} catch (IOException e) {
//				}
//			} else {
//				// 发送消息
//				sendMsg(selectionKey, msg);
//			}
			sendMsg(selectionKey, msg);
		}
	}

	/**
	 * 处理客户端发来的信息
	 * 
	 * @param key
	 * @param msgJson
	 */
	public static void processMsg(SelectionKey key, String msgJson) {
		MsgBean msg = new MsgBean();
		msg.setMsg(msgJson);
		msg.setTime(System.currentTimeMillis());
		sendMsg(key, msg);
//		JSONObject json = JSONObject.parseObject(msgJson);
//		int type = json.getInteger("type"); // 获取消息类型
//
//		switch (type) {
//		case MsgBean.TYPE_REG:
//			// 注册
//			break;
//
//		case MsgBean.TYPE_LOGIN:
//			// 登录
//			
//			break;
//		}

	}

}
