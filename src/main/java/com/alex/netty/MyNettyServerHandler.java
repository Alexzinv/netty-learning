package com.alex.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author _Alexzinv_
 * @since 2022/5/9
 * 自定义的Handler需要继承Netty规定好的HandlerAdapter
 */
public class MyNettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收netty发送过来的消息
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("接收到来自" + ctx.channel().remoteAddress() + "的消息 => " + buf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 发送消息给客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务端已收到消息 --->", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常关闭通道
        ctx.close();
    }
}
