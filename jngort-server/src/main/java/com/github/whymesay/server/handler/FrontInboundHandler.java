package com.github.whymesay.server.handler;

import com.github.whymesay.server.FrontClientHolder;
import com.github.whymesay.server.JngorkClientHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 前端数据传入
 *
 * @author whymesay
 * @date 2021/7/11 22:57
 */
public class FrontInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FrontClientHolder.frontClientChannel = ctx.channel();
        //转发给客户端
        JngorkClientHolder.jngorkClientChannel.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
