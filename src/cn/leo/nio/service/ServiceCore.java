package cn.leo.nio.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import cn.leo.nio.db.MysqlHelper;
import cn.leo.nio.processer.Reader;
import cn.leo.nio.processer.ReaderManager;
import cn.leo.nio.processer.Writer;
import cn.leo.nio.utils.Logger;
import cn.leo.nio.utils.ThreadPool;

public class ServiceCore extends Thread {
	private static final int BUFFER_CACHE = 1024;// ��������С
	private static final int TIMEOUT = 3000; // ��ʱʱ��3��
	private static ServiceListener mListener; // ������������

	public static void StartService(ServiceListener listener) {
		mListener = listener;
		new ServiceCore().start();
	}

	@Override
	public void run() {
		selector();// ����������
	}

	private void selector() {
		Selector selector = null; // nio ѡ����
		ServerSocketChannel ssc = null; // nio Ƶ������
		try {
			selector = Selector.open(); // ����ѡ����
			ssc = ServerSocketChannel.open(); // ����Ƶ��
			ssc.socket().bind(new InetSocketAddress(MysqlHelper.port)); // Ƶ���󶨼����˿�
			ssc.configureBlocking(false); // ����������
			ssc.register(selector, SelectionKey.OP_ACCEPT); // Ƶ��ע�ᵽѡ����
			Logger.i("��������������");
			while (true) {
				if (selector.select(TIMEOUT) == 0) { // �ȴ���ʱ��û��֪ͨ��������һ��ѭ��
					continue;
				}
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator(); // ����Ƶ��ѡ����������֪ͨ�������Ӿͻ�ȡ
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					try {
						if (key.isAcceptable()) { // �������������
							handleAccept(key);
						} else if (key.isReadable()) { // ����Ƕ�ȡ����
							handleRead(key);
						} else if (key.isWritable() && key.isValid()) { // ��д����
							handleWrite(key);
						} else if (key.isConnectable()) { // �ж��Ƿ������
							Logger.i("isConnectable = true");
						}
						iter.remove(); // �����Ӷ����Ƴ�
					} catch (Exception e) {
						key.cancel();
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			Logger.i("�����������쳣�����������Ϣ��");
		} finally {
			try {
				if (selector != null) {
					selector.close();
				}
				if (ssc != null) {
					ssc.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ���������¼�
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void handleAccept(final SelectionKey key) {
		ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
		SelectionKey register = null;
		try {
			SocketChannel sc = ssChannel.accept(); // ������������,�����µ�Ƶ��
			sc.configureBlocking(false); // ����������
			register = sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUFFER_CACHE));
			if (mListener != null) {
				mListener.onNewConnectComing(register);
			}
		} catch (Exception e) {
			if (mListener != null) {
				mListener.onConnectInterrupt(key);
			}
		}

	}

	/**
	 * �����ȡ����
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void handleRead(final SelectionKey key) {
		Reader reader = ReaderManager.getReader(key);
		if (reader == null) {
			reader = new Reader(key, mListener);
			ReaderManager.putReader(key, reader);
			ThreadPool.execute(reader);
		}
	}

	/**
	 * ����д������
	 * 
	 * @param key
	 * @throws IOException
	 */
	private void handleWrite(final SelectionKey key) {
		ByteBuffer buf = (ByteBuffer) key.attachment(); // ��ȡkey���ӵĻ�����
		SocketChannel sc = (SocketChannel) key.channel(); // ��ȡkey��Ƶ��
		try {
			buf.flip();// ���û�����limit
			while (buf.hasRemaining()) {
				if (sc.isConnected())
					sc.write(buf); // д�뻺�������ݵ�Ƶ��
			}
			buf.compact(); //
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

	/**
	 * ��������
	 * 
	 * @param bytes
	 */
	public static void sendMsg(SelectionKey key, byte[] bytes) {
		ThreadPool.execute(new Writer(mListener, key, bytes));
	}

}
