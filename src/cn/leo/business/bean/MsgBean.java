package cn.leo.business.bean;

import com.google.gson.Gson;

public class MsgBean {
    public static final int TYPE_SYS = 0; // 系统消息
    public static final int TYPE_PUB = 1; // 群聊消息
    public static final int TYPE_PRI = 2; // 私聊消息
    public static final int TYPE_REG = 3; // 注册消息
    public static final int TYPE_LOGIN = 4; // 登录消息
    public static final int TYPE_PAINT = 5; // 画画消息
    public static final int TYPE_GAME = 6; // 游戏指令，根据消息首字母判断类型
    private String msg; // 消息内容
    private long time; // 消息时间
    private int type; // 消息类型
    private long area; // 房间号 如果有群或者房间的话

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
