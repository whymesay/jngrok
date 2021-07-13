package com.github.whymesay.server;

import com.github.whymesay.server.handler.JngorkClientInHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author whymesay
 * @date 2021/7/12 22:45
 */
public class JngorkClientHolder {

    public static Channel jngorkClientChannel;
    public static ChannelFuture future;

    public void init() {
        {
            // 处理TCP连接请求
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            // 处理I/O事件
            EventLoopGroup workGroup = new NioEventLoopGroup();
            try {
                // 用于引导和绑定服务器
                ServerBootstrap bootstrap = new ServerBootstrap();
                //将上面的线程组加入到 bootstrap 中
                bootstrap.group(bossGroup, workGroup)
                        //将通道设置为异步的通道
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                // 因为 NettyServerHandler 被标注为 @Sharable，所以可以使用相同的实例
                                socketChannel.pipeline()
                                        .addLast(new LoggingHandler(LogLevel.DEBUG))
                                        .addLast(new JngorkClientInHandler());
                            }
                        })
                        .option(ChannelOption.TCP_NODELAY, true)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                // 异步绑定服务器，调用 sync() 方法阻塞等待直到绑定完成。
                future = bootstrap.bind(2333).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }

        }
    }

}
