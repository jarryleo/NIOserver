package cn.leo.nio.processer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cn.leo.nio.service.ServiceListener;

public class Reader implements Runnable {
	private static final int INT_LENGTH = 4;

	private SelectionKey key;
	private ServiceListener mListener;

	public Reader(SelectionKey key, ServiceListener mListener) {
		super();
		this.key = key;
		this.mListener = mListener;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Thread-Reader");
		SocketChannel sc = (SocketChannel) key.channel(); // ��ȡkey��Ƶ��
		ByteBuffer headBuffer = ByteBuffer.allocate(INT_LENGTH); // 1��intֵ��ͷ�ֽڴ洢���ݳ���
		ByteBuffer buf = (ByteBuffer) key.attachment(); // ��ȡkey�ĸ��Ӷ�����Ϊ���ӵĻ�������
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			while (sc.read(headBuffer) == INT_LENGTH) {
				int dataLength = headBuffer.getInt(0); // ��ȡ����ͷ��4���ֽڵ�intֵ���������ݳ���
				headBuffer.clear();
				byte[] bytes;
				int receiveLength = 0; // �ѽ��ܳ���
				int bytesRead = 0;// ��ȡƵ���ڵ����ݵ�������
				while (receiveLength < dataLength) {
					if (dataLength - receiveLength < buf.capacity()) { // ������һ���ֱȻ�����С���������������С
						buf.limit(dataLength - receiveLength);
					}
					bytesRead = sc.read(buf); // TODO ���������BUGҪ����
					buf.flip();// ���û�����limit

					if (bytesRead < 1) { // ��ȡ���������˳�
						break;
					}

					if (receiveLength + bytesRead > dataLength) { // ������ܵ����ݴ���ָ������
						bytes = new byte[dataLength - receiveLength]; // ���µ�����Ϊʣ�����ݳ���
					} else {
						bytes = new byte[bytesRead]; // ����Ϊ��ȡ����
					}
					buf.get(bytes);
					baos.write(bytes);
					buf.clear();// ��ջ�����
					receiveLength += bytes.length; // �Ѷ�ȡ�����ݳ���
				}
				if (mListener != null) {
					mListener.onDataArrived(key, baos.toByteArray());
				}
				baos.reset();
				if (bytesRead == -1) { // �������Ͽ����ӣ��ر�Ƶ��
					sc.close();
				}
			}
			ReaderManager.remove(key);
		} catch (IOException e) {
			try {
				sc.close();
			} catch (IOException e1) {
				// e1.printStackTrace();
			}
			if (mListener != null) {
				mListener.onConnectInterrupt(key);
			}
		}
	}

}
