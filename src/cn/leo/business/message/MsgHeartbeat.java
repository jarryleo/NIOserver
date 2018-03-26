package cn.leo.business.message;

import cn.leo.business.bean.MsgBean;
import cn.leo.business.constant.MsgType;
import cn.leo.business.constant.MsgCode;

public class MsgHeartbeat extends Thread {
    private static final int INTERVAL = 1000 * 10; // 10秒
    private static final MsgBean SYSMSG = new MsgBean();

    private MsgHeartbeat() {
        super();
        SYSMSG.setType(MsgType.SYS);
        SYSMSG.setCode(MsgCode.HEART.getCode());
    }

    /**
     * 启动心跳检测机制
     */
    public static void StartHeartbeat() {
        new MsgHeartbeat().start();
    }

    @Override
    public void run() {
        while (true) {
            SYSMSG.setTime(System.currentTimeMillis());
            sendHeartMsg();
            try {
                sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送心跳信息
     */
    private void sendHeartMsg() {
        MsgManager.sendMsgToAll(SYSMSG);
    }
}
