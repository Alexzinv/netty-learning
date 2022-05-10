package com.alex.netty.b;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author _Alexzinv_
 * @since 2022/5/10
 * Nio原始方式
 */
public class PlainNioServer {
    public void serve(int port) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        ServerSocket socket = channel.socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        socket.bind(inetSocketAddress);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("hi\r\n".getBytes());
        for (;;){
            try {
                selector.select();
            }catch (IOException e){
                e.printStackTrace();
                break;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if(key.isAcceptable()){
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel client = serverChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                        System.out.println("Accepted connection from " + client);
                    }
                    if(key.isWritable()){
                        SocketChannel client = (SocketChannel)key.channel();
                        ByteBuffer buffer = (ByteBuffer)key.attachment();
                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                        client.close();
                    }
                }catch (IOException e){
                    key.cancel();
                    try {
                        key.channel().close();
                    }catch (IOException ignore){}
                }
            }
        }
    }
}
