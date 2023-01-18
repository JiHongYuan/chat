package org.github.jhy.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 聊天室服务端
 */
@Slf4j
public class NettyChatServer {

    private final int port;
    private final int backlog;
    private final boolean keepalive;
    @Getter
    private Channel channel;

    public NettyChatServer(int port) {
        this.port = port;
        this.backlog = 128;
        this.keepalive = true;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = getBossGroup();
        EventLoopGroup workerGroup = getWorkerGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, backlog)
                    .childOption(ChannelOption.SO_KEEPALIVE, keepalive)
                    .childHandler(new ChatChannelInitializer());

            ChannelFuture future = serverBootstrap.bind(port).sync();
            boolean success = future.isSuccess();

            if (success) {
                this.channel = future.channel();
                log.info("Server start success port: {}", port);
            } else {
                Throwable cause = future.cause();
                log.error(cause.getMessage(), cause);
            }

            this.channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    protected EventLoopGroup getBossGroup() {
        return new NioEventLoopGroup(1);
    }

    protected EventLoopGroup getWorkerGroup() {
        return new NioEventLoopGroup();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyChatServer(9999).run();
    }

}
