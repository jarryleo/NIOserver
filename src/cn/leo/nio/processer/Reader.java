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
		SocketChannel sc = (SocketChannel) key.channel(); // 获取key的频道
		ByteBuffer headBuffer = ByteBuffer.allocate(INT_LENGTH); // 1个int值的头字节存储数据长度
		ByteBuffer buf = (ByteBuffer) key.attachment(); // 获取key的附加对象（因为附加的缓冲区）
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			while (sc.read(headBuffer) == INT_LENGTH) {
				int dataLength = headBuffer.getInt(0); // 读取数据头部4个字节的int值表述的数据长度
				headBuffer.clear();
				byte[] bytes;
				int receiveLength = 0; // 已接受长度
				int bytesRead = 0;// 读取频道内的数据到缓冲区
				while (receiveLength < dataLength) {
					if (dataLength - receiveLength < buf.capacity()) { // 如果最后一部分比缓冲区小，则调整缓冲区大小
						buf.limit(dataLength - receiveLength);
					}
					bytesRead = sc.read(buf); // TODO 这里可能有BUG要处理
					buf.flip();// 重置缓冲区limit

					if (bytesRead < 1) { // 读取不到数据退出
						break;
					}

					if (receiveLength + bytesRead > dataLength) { // 如果接受的数据大于指定长度
						bytes = new byte[dataLength - receiveLength]; // 则新的数据为剩下数据长度
					} else {
						bytes = new byte[bytesRead]; // 否则为读取长度
					}
					buf.get(bytes);
					baos.write(bytes);
					buf.clear();// 清空缓冲区
					receiveLength += bytes.length; // 已读取的数据长度
				}
				if (mListener != null) {
					mListener.onDataArrived(key, baos.toByteArray());
				}
				baos.reset();
				if (bytesRead == -1) { // 服务器断开连接，关闭频道
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
