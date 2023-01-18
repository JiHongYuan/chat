package org.github.jhy.chat.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.common.EventMessage;

/**
 * 聊天室业务处理类
 */
@Slf4j
public class NettyChatServerHandler extends SimpleChannelInboundHandler<EventMessage> {

    private final MessageHandler messageHandler = new MessageHandler();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        messageHandler.handlerActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        messageHandler.handlerInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EventMessage msg) throws Exception {
        messageHandler.handlerRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        messageHandler.handlerCaught(ctx, cause);
    }

}
