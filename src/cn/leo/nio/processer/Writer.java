package cn.leo.nio.processer;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cn.leo.nio.service.ServiceListener;

public class Writer implements Runnable {
    private static final int INT_LENGTH = 4;
    private static final int BUFFER_CACHE = 1024 * 64;// 缓冲区大小
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
            if (sc.isConnected()) { // 如果连接成功，则循环发送消息
                int length = bytes.length; // 要发送数据的长度，如果长度大于缓冲区就分段发送
                int start = 0; // 分段起始点
                while (length > 0) {
                    int part = 0; // 每段大小
                    if (length >= (BUFFER_CACHE - INT_LENGTH)) {
                        part = (BUFFER_CACHE - INT_LENGTH);
                    } else {
                        part = length % (BUFFER_CACHE - INT_LENGTH); // 最后一段大小，
                    }
                    byte[] b = new byte[part]; // 分段数组

                    System.arraycopy(bytes, start, b, 0, part);// 复制分段数据
                    // 写入数据内容
                    buffer.clear(); // 清除缓冲区
                    if (start == 0) {
                        buffer.putInt(length); // 第一次分段头写入总数据长度
                    }
                    buffer.put(b);// 把字符串的字节数据写入缓冲区
                    buffer.flip();// 重置缓冲区limit
                    while (buffer.hasRemaining()) {
                        sc.write(buffer); // 缓冲区数据写入频道
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
