package cn.leo.nio.service;

import cn.leo.nio.processer.Reader;
import cn.leo.nio.processer.ReaderManager;
import cn.leo.nio.processer.Writer;
import cn.leo.nio.utils.Logger;
import cn.leo.nio.utils.ThreadPool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServiceCore extends Thread {
    private static final int BUFFER_CACHE = 1024;// 缓冲区大小
    private static final int TIMEOUT = 3000; // 超时时间3秒
    private static ServiceListener mListener; // 服务器监听器
    private static int mPort;

    public static void StartService(ServiceListener listener, int port) {
        mListener = listener;
        mPort = port;
        new ServiceCore().start();
    }

    @Override
    public void run() {
        selector();// 开启服务器
    }

    private void selector() {
        Selector selector = null; // nio 选择器
        ServerSocketChannel ssc = null; // nio 频道监听
        try {
            selector = Selector.open(); // 开启选择器
            ssc = ServerSocketChannel.open(); // 开启频道
            ssc.socket().bind(new InetSocketAddress(mPort)); // 频道绑定监听端口
            ssc.configureBlocking(false); // 无阻塞设置
            ssc.register(selector, SelectionKey.OP_ACCEPT); // 频道注册到选择器
            Logger.i("服务器已启动！");
            while (true) {
                if (selector.select(TIMEOUT) == 0) { // 等待超时且没有通知，进入下一个循环
                    continue;
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator(); // 遍历频道选择器的连接通知，有连接就获取
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    try {
                        if (key.isAcceptable()) { // 如果是连接类型
                            handleAccept(key);
                        } else if (key.isReadable()) { // 如果是读取数据
                            handleRead(key);
                        } else if (key.isWritable() && key.isValid()) { // 可写数据
                            handleWrite(key);
                        } else if (key.isConnectable()) { // 判断是否可连接
                            Logger.i("isConnectable = true");
                        }
                        iter.remove(); // 处理后从队列移除
                    } catch (Exception e) {
                        key.cancel();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.i("服务器发生异常，请检查错误信息！");
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
     * 处理连接事件
     *
     * @param key
     * @throws IOException
     */
    private void handleAccept(final SelectionKey key) {
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        SelectionKey register = null;
        try {
            SocketChannel sc = ssChannel.accept(); // 接收连接请求,产生新的频道
            sc.configureBlocking(false); // 设置无阻塞
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
     * 处理读取数据
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
     * 处理写入数据
     *
     * @param key
     * @throws IOException
     */
    private void handleWrite(final SelectionKey key) {
        ByteBuffer buf = (ByteBuffer) key.attachment(); // 拿取key附加的缓冲区
        SocketChannel sc = (SocketChannel) key.channel(); // 拿取key的频道
        try {
            buf.flip();// 重置缓冲区limit
            while (buf.hasRemaining()) {
                if (sc.isConnected())
                    sc.write(buf); // 写入缓冲区数据到频道
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
     * 发送数据
     *
     * @param bytes
     */
    public static void sendMsg(SelectionKey key, byte[] bytes) {
        ThreadPool.execute(new Writer(mListener, key, bytes));
    }

}
