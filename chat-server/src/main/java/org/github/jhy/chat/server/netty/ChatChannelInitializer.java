package org.github.jhy.chat.server.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


/**
 * @author jihongyuan
 * @date 2023/1/6 15:06
 */
public class ChatChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new EventMessageCodec());
        ch.pipeline().addLast(new NettyChatServerHandler());
    }

}
