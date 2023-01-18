package org.github.jhy.chat.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.github.jhy.chat.server.handler.EventMessageCodec;
import org.github.jhy.chat.server.handler.NettyChatServerHandler;


/**
 * @author jihongyuan
 * @date 2023/1/6 15:06
 */
public class ChatChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new EventMessageCodec());
        ch.pipeline().addLast(new NettyChatServerHandler());
    }

}
