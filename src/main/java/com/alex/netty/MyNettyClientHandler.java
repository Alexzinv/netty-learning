package com.alex.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author _Alexzinv_
 * @since 2022/5/9
 */
public class MyNettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发消息到服务端
        ctx.writeAndFlush(Unpooled.copiedBuffer("testaaaaaaaaaaaaaaaaa", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("接收到来自服务端" + ctx.channel().remoteAddress() + "的消息 => "
                + buf.toString(CharsetUtil.UTF_8));
    }
}
