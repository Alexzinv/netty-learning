package com.alex.netty.a;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author _Alexzinv_
 * @since 2022/5/9
 */
public class MyNettyServer {
    public static void main(String[] args) {
        // 创建两个线程组，可自定义线程数
        // bossGroup用于监听客户端连接，专门负责与客户端创建连接，并把连接注册到workerGroup的Selector中
        // workerGroup 用于处理每一个连接的读写时间
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(16);
        try {
            // 创建服务端启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置两个线程组
            bootstrap.group(bossGroup, workerGroup)
                    // 设置nio channel
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列得到连接个数 bossGroup线程组
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态 workerGroup线程组
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            channel.pipeline().addLast(new MyNettyServerHandler());
                        }
                    });
            System.out.println("----------服务端已经准备就绪...------------");
            // 绑定端口号，启动服务端
            ChannelFuture future = bootstrap.bind(8888).sync();
            // 对关闭通道进行监听
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
