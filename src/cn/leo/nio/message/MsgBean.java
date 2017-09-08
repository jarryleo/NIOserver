package cn.leo.nio.message;

import cn.leo.nio.user.UserInfoBean;

public class MsgBean {
	public static final int TYPE_SYS = 0; // ϵͳ��Ϣ
	public static final int TYPE_PUB = 1; // Ⱥ����Ϣ
	public static final int TYPE_PRI = 2; // ˽����Ϣ
	public static final int TYPE_REG = 3; // ˽����Ϣ
	public static final int TYPE_LOGIN = 4; // ˽����Ϣ
	private String msg; // ��Ϣ����
	private long time; // ��Ϣʱ��
	private int type; // ��Ϣ����
	private long area; // ����� �����Ⱥ���߷���Ļ�
	private UserInfoBean from; // ���ͷ�
	private UserInfoBean target;// ���ܷ�

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
