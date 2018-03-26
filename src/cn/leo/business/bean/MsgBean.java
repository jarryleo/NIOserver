package cn.leo.business.bean;

import cn.leo.business.constant.MsgType;
import com.google.gson.Gson;

public class MsgBean {
    private MsgType type; // 消息类型
    private String msg; // 消息内容
    private int code; // 消息错误码
    private long time; // 消息时间
    private long area; // 房间号 如果有群或者房间的话

    public MsgBean() {
        time = System.currentTimeMillis();
    }

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

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
