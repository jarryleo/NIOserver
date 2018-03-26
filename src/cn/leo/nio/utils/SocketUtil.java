package cn.leo.nio.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SocketUtil {
    /**
     * 获取SelectionKey 的 ip 地址
     *
     * @param key
     * @return
     */
    public static String getSelectionKeyIp(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            InetSocketAddress address = (InetSocketAddress) socketChannel.getRemoteAddress();
            return address.getHostString();
        } catch (IOException e) {

        }
        return null;
    }
}
