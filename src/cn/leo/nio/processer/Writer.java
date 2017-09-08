package cn.leo.nio.processer;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cn.leo.nio.service.ServiceListener;

public class Writer implements Runnable {
	private static final int INT_LENGTH = 4;
	private static final int BUFFER_CACHE = 1024;// ��������С
	private ServiceListener mListener;
	private SelectionKey key;
	private byte[] bytes;

	public Writer(ServiceListener mListener, SelectionKey key, byte[] bytes) {
		super();
		this.mListener = mListener;
		this.key = key;
		this.bytes = bytes;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Thread-Writer");
		SocketChannel sc = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_CACHE);
		try {
			if (sc.isConnected()) { // ������ӳɹ�����ѭ��������Ϣ
				int length = bytes.length; // Ҫ�������ݵĳ��ȣ�������ȴ��ڻ������ͷֶη���
				int start = 0; // �ֶ���ʼ��
				while (length > 0) {
					int part = 0; // ÿ�δ�С
					if (length >= (BUFFER_CACHE - INT_LENGTH)) {
						part = (BUFFER_CACHE - INT_LENGTH);
					} else {
						part = length % (BUFFER_CACHE - INT_LENGTH); // ���һ�δ�С��
					}
					byte[] b = new byte[part]; // �ֶ�����

					System.arraycopy(bytes, start, b, 0, part);// ���Ʒֶ�����
					// д����������
					buffer.clear(); // ���������
					if (start == 0) {
						buffer.putInt(length); // ��һ�ηֶ�ͷд�������ݳ���
					}
					buffer.put(b);// ���ַ������ֽ�����д�뻺����
					buffer.flip();// ���û�����limit
					while (buffer.hasRemaining()) {
						sc.write(buffer); // ����������д��Ƶ��
					}
					start += part;
					length -= part;
				}
			}
		} catch (Exception e) {
			if (mListener != null) {
				mListener.onConnectInterrupt(key);
			}
		}

	}

}
