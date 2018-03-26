package cn.leo.business.bean;

import com.google.gson.Gson;

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
    private int icon;
    private int score;
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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
