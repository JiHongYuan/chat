package org.github.jhy.chat.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.EventType;
import org.github.jhy.chat.server.model.UserSession;
import org.github.jhy.chat.server.model.UserStatus;
import org.github.jhy.chat.server.support.ChannelClientRegister;
import org.github.jhy.chat.server.support.ChatUserRegister;

/**
 * @author jihongyuan
 * @date 2023/1/18 10:21
 */
@Slf4j
public class MessageHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final ChannelClientRegister channelClientRegister = new ChannelClientRegister();
    private static final ChatUserRegister chatUserOnLineRegister = new ChatUserRegister();

    public void handlerActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channelClientRegister.register(channel.id().asShortText(), channel);
    }

    public void handlerInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String sessionId = channel.id().asShortText();
        channelClientRegister.unRegister(sessionId);
    }

    public void handlerCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel channel = ctx.channel();
        channelClientRegister.unRegister(channel.id().asShortText());
        log.error(cause.getMessage(), cause);
    }

    public void handlerRead(ChannelHandlerContext ctx, EventMessage msg) {
        final EventType eventType = msg.getEventType();

        switch (eventType) {
            case REGISTER -> new RegisterHandler().handle(ctx, msg);
            case MESSAGE -> System.out.println("");
        }
    }

    interface IEventHandler {
        default void handle(ChannelHandlerContext ctx, EventMessage msg) {
            handlerRequest(ctx, msg);
            handlerResult(ctx, msg);
        }

        void handlerRequest(ChannelHandlerContext ctx, EventMessage msg);

        void handlerResult(ChannelHandlerContext ctx, EventMessage msg);
    }

    private static class RegisterHandler implements IEventHandler {
        public void handlerRequest(ChannelHandlerContext ctx, EventMessage msg) {
            UserSession userSession;
            if ((userSession = chatUserOnLineRegister.get(msg.getTo())) != null) {
                userSession.setStatus(UserStatus.OFFLINE);
            } else {
                userSession = new UserSession();
                userSession.setSessionId(ctx.channel().id().asShortText());
                userSession.setUsername(msg.getTo());
                userSession.setStatus(UserStatus.OFFLINE);
            }
            chatUserOnLineRegister.register(msg.getTo(), userSession);
        }

        public void handlerResult(ChannelHandlerContext ctx, EventMessage msg) {
            EventMessage retMsg = EventMessage.builderEventType(msg.getMsgId(), EventType.SERVER_ANSWER);
            retMsg.setTo("SERVER");
            retMsg.setFrom(msg.getTo());
            ctx.channel().writeAndFlush(retMsg).channel().newPromise();
        }
    }

}
