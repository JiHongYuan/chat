package org.github.jhy.chat.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.EventType;
import org.github.jhy.chat.common.model.Message;
import org.github.jhy.chat.common.model.MessageUser;
import org.github.jhy.chat.server.model.UserSession;
import org.github.jhy.chat.common.model.UserStatus;
import org.github.jhy.chat.server.support.ChannelClientRegister;
import org.github.jhy.chat.server.support.ChatUserRegister;

import java.util.Collection;
import java.util.List;

/**
 * @author jihongyuan
 * @date 2023/1/18 10:21
 */
@Slf4j
public class MessageHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final ChannelClientRegister channelClientRegister = new ChannelClientRegister();
    private static final ChatUserRegister chatUserRegister = new ChatUserRegister();

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
            case MESSAGE -> new MsgHandler().handle(ctx, msg);
            case GET_ON_LINE_USER -> new GetUserHandler(UserStatus.OFFLINE).handle(ctx, msg);
            case GET_USER -> new GetUserHandler().handle(ctx, msg);
        }
    }

    interface IEventHandler {

        /**
         * 事件处理
         *
         * @param ctx ctx
         * @param msg event message
         */
        default void handle(ChannelHandlerContext ctx, EventMessage msg) {
            EventMessage retMsg = getEventMessage(msg);
            handlerRequest(ctx, msg, retMsg);
            handlerResult(ctx, msg, retMsg);
        }

        /**
         * 处理请求
         */
        default void handlerRequest(ChannelHandlerContext ctx, EventMessage msg, EventMessage retMsg) {
        }

        /**
         * 处理返回
         */
        default void handlerResult(ChannelHandlerContext ctx, EventMessage msg, EventMessage retMsg) {
            ctx.channel().writeAndFlush(retMsg);
        }

        default EventMessage getEventMessage(EventMessage msg){
            return null;
        }

    }

    private static class RegisterHandler implements IEventHandler {

        @Override
        public void handlerRequest(ChannelHandlerContext ctx, EventMessage msg, EventMessage retMsg) {
            UserSession userSession;
            if ((userSession = chatUserRegister.get(msg.getFrom())) != null) {
                userSession.setStatus(UserStatus.OFFLINE);
            } else {
                userSession = new UserSession();
                userSession.setSessionId(ctx.channel().id().asShortText());
                userSession.setUsername(msg.getFrom());
                userSession.setStatus(UserStatus.OFFLINE);
            }
            retMsg.setBody(new Message("0", "登录成功"));
            chatUserRegister.register(msg.getFrom(), userSession);
        }

        @Override
        public EventMessage getEventMessage(EventMessage msg) {
            EventMessage retMsg = EventMessage.builderEventType(msg.getMsgId(), msg.getEventType());
            retMsg.setFrom("SERVER");
            retMsg.setTo(msg.getFrom());
            return retMsg;
        }
    }

    @AllArgsConstructor
    private static class GetUserHandler implements IEventHandler {

        private final UserStatus userStatus;

        public GetUserHandler() {
            this.userStatus = null;
        }

        @Override
        public EventMessage getEventMessage(EventMessage msg) {
            EventMessage retMsg = EventMessage.builderEventType(msg.getMsgId(), msg.getEventType());
            retMsg.setFrom("SERVER");
            retMsg.setTo(msg.getFrom());

            List<MessageUser> userList;
            Collection<UserSession> users = chatUserRegister.getUsers();
            if (userStatus == null) {
                userList = users.stream()
                        .filter(k -> !k.getUsername().equals(msg.getFrom()))
                        .map(k -> new MessageUser(k.getUsername(), k.getStatus())).toList();
            } else {
                userList = users.stream()
                        .filter(k -> userStatus == k.getStatus() || !k.getUsername().equals(msg.getFrom()))
                        .map(k -> new MessageUser(k.getUsername(), k.getStatus()))
                        .toList();
            }
            retMsg.setBody(userList);
            return retMsg;
        }
    }

    @AllArgsConstructor
    private static class MsgHandler implements IEventHandler {

        @Override
        public void handlerResult(ChannelHandlerContext ctx, EventMessage msg, EventMessage retMsg) {
            UserSession userSession;
            if ((userSession = chatUserRegister.get(msg.getTo())) != null) {
                Channel channel = channelClientRegister.get(userSession.getSessionId());
                channel.writeAndFlush(msg);
            }
        }

    }

}
