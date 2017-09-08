package cn.leo.nio.message;

public class MsgHeartbeat extends Thread {
	private static final int INTERVAL = 1000 * 10; // 10��
	private static final MsgBean SYSMSG = new MsgBean();

	private MsgHeartbeat() {
		super();
		SYSMSG.setMsg("Heart");
		SYSMSG.setType(MsgBean.TYPE_SYS);
	}

	/**
	 * ��������������
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
	 * ����������Ϣ
	 */
	private void sendHeartMsg() {
		MsgManager.sendMsgToAll(SYSMSG);
	}
}
