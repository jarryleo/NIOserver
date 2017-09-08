package cn.leo.nio.message;

import cn.leo.nio.user.UserInfoBean;

public class MsgBean {
	public static final int TYPE_SYS = 0; // 系统消息
	public static final int TYPE_PUB = 1; // 群聊消息
	public static final int TYPE_PRI = 2; // 私聊消息
	public static final int TYPE_REG = 3; // 私聊消息
	public static final int TYPE_LOGIN = 4; // 私聊消息
	private String msg; // 消息内容
	private long time; // 消息时间
	private int type; // 消息类型
	private long area; // 房间号 如果有群或者房间的话
	private UserInfoBean from; // 发送方
	private UserInfoBean target;// 接受方

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getArea() {
		return area;
	}

	public void setArea(long area) {
		this.area = area;
	}

	public UserInfoBean getFrom() {
		return from;
	}

	public void setFrom(UserInfoBean from) {
		this.from = from;
	}

	public UserInfoBean getTarget() {
		return target;
	}

	public void setTarget(UserInfoBean target) {
		this.target = target;
	}

}
