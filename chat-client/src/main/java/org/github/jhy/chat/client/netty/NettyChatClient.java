package org.github.jhy.chat.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Collections;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.ApplicationContext;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.EventType;
import org.github.jhy.chat.common.model.Message;
import org.github.jhy.chat.common.model.MessageUser;

import java.util.List;

/**
 * 聊天室服务端
 */
@Slf4j
public class NettyChatClient {

    private final String ip;
    private final int port;

    @Getter
    private Channel channel;

    @Getter
    private EventLoopGroup clientGroup;

    public NettyChatClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void run() throws InterruptedException {
        this.clientGroup = getEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChatChannelInitializer());
        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        this.channel = channelFuture.channel();
    }

    protected EventLoopGroup getEventLoopGroup() {
        return new NioEventLoopGroup();
    }

    public ChannelFuture send(EventMessage eventMessage) {
        return channel.writeAndFlush(eventMessage);
    }

    /**
     * 发送同步消息
     *
     * @param eventMessage msg
     * @return server response
     */
    public EventMessage sendSync(EventMessage eventMessage) {
        SyncFuture<EventMessage> syncFuture = new SyncFuture<>();
        ApplicationContext.FUTURE_CACHE.put(eventMessage.getMsgId(), syncFuture);
        send(eventMessage);
        try {
            return syncFuture.get();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * 发送获取用户列表
     *
     * @param from current user
     * @return msgId
     */
    @SuppressWarnings("unchecked")
    public List<MessageUser> getUserList(String from) {
        EventMessage eventMessage = EventMessage.builderEventType(EventType.GET_USER);
        eventMessage.setFrom(from);
        eventMessage.setTo("SERVER");

        try {
            EventMessage message = sendSync(eventMessage);
            return (List<MessageUser>) message.getBody();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public EventMessage sendMsg(String from, String to, String msg) {
        EventMessage eventMessage = EventMessage.builderEventType(EventType.MESSAGE);
        eventMessage.setFrom(from);
        eventMessage.setTo(to);
        eventMessage.setBody(new Message("0", msg));
        try {
            send(eventMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return eventMessage;
    }

}
