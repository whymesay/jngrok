package com.github.whymesay.client.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author whymesay
 * @date 2021/7/13 22:12
 */
public class ProxyForwardUtil {
    /**
     * 后端服务bootstrap
     */
    static Bootstrap backendBootstrap;

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            backendBootstrap = new Bootstrap();
            backendBootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new BackendInHandler());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Channel connect(String host, int port) {
        ChannelFuture sync = null;
        try {
            sync = backendBootstrap.connect(host, port).addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("success");
                } else {
                    System.out.println("fail");
                }
            }).sync();
            return sync.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
