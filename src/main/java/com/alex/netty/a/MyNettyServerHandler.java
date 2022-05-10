package com.alex.netty.a;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author _Alexzinv_
 * @since 2022/5/9
 * 自定义的Handler需要继承Netty规定好的HandlerAdapter
 */
public class MyNettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收客户端发送过来的消息 对每个传入的消息都会调用
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("接收到来自" + ctx.channel().remoteAddress() + "的消息 => " + buf.toString(CharsetUtil.UTF_8));

        // 任务队列
        // Handler处理器有一些长时间的业务处理，可以交给taskQueue异步处理
        // ctx.channel().eventLoop().execute(() -> {
        //     try {
        //         // 长时间操作，不至于长时间的业务操作导致Handler阻塞
        //         // 模拟长时业务
        //         Thread.sleep(10000);
        //         System.out.println("长时业务执行 ...");
        //     }catch (Exception e){
        //         e.printStackTrace();
        //     }
        // });

        // 延时队列 scheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    // 长时间操作，不至于长时间的业务操作导致Handler阻塞
                    // 模拟长时业务
                    Thread.sleep(10000);
                    System.out.println("长时业务执行 ...");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 6, TimeUnit.SECONDS);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 发送消息给客户端 在ChannelRead的最后一条批量消息之后
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息 --->", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常关闭通道
        cause.printStackTrace();
        ctx.close();
    }
}
