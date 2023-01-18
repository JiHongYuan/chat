package org.github.jhy.chat.client.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.github.jhy.chat.client.handler.EventMessageCodec;
import org.github.jhy.chat.client.handler.NettyChatClientHandler;


/**
 * @author jihongyuan
 * @date 2023/1/6 15:06
 */
public class ChatChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new EventMessageCodec());
        ch.pipeline().addLast(new NettyChatClientHandler());
    }

}
