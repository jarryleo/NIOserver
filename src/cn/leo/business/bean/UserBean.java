package cn.leo.business.bean;

import cn.leo.nio.utils.SocketUtil;
import com.google.gson.Gson;

import java.nio.channels.SelectionKey;

public class UserBean {
    //用户所在房间
    private RoomBean room;
    //用户连接对象，用来通信
    private SelectionKey selectionKey;
    //用户姓名
    private String userName;
    //用户ip
    private String ip;
    //用户id
    private int userId;
    //用户性别
    private int sex;
    //用户头像
    private int icon;
    //用户积分
    private int score;
    //用户连接时间（上次心跳时间）
    private long connectTime;

    public UserBean(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        this.connectTime = System.currentTimeMillis();
        ip = SocketUtil.getSelectionKeyIp(selectionKey);
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setConnectTime(long connectTime) {
        this.connectTime = connectTime;
    }

    public RoomBean getRoom() {
        return room;
    }

    public void setRoom(RoomBean room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    //更新心跳时间
    public void refreshHeart(MsgBean msgBean) {
        this.connectTime = msgBean.getTime();
    }
}
