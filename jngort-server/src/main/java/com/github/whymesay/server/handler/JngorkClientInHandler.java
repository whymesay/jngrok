package com.github.whymesay.server.handler;

import com.github.whymesay.server.FrontClientHolder;
import com.github.whymesay.server.JngorkClientHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author whymesay
 * @date 2021/7/11 22:57
 */
public class JngorkClientInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JngorkClientHolder.jngorkClientChannel = ctx.channel();
        if (FrontClientHolder.frontClientChannel != null) {
            FrontClientHolder.frontClientChannel.writeAndFlush(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
