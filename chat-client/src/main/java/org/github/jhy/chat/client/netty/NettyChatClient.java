package org.github.jhy.chat.client.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.EventType;
import org.github.jhy.chat.common.model.MessageUser;

import java.util.List;

/**
 * 聊天室服务端
 */
@Slf4j
public class NettyChatClient {

    private static final ObjectMapper mapper = new ObjectMapper();

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
    public EventMessage sendSync(EventMessage eventMessage) throws InterruptedException {
        SyncFuture<EventMessage> syncFuture = new SyncFuture<>();
        ApplicationContext.futureCache.put(eventMessage.getMsgId(), syncFuture);
        send(eventMessage);
        return syncFuture.get();
    }

    /**
     * 发送注册消息
     *
     * @param messageUser user info
     * @return msgId
     */
    public EventMessage sendRegister(MessageUser messageUser) {
        EventMessage eventMessage = EventMessage.builderEventType(EventType.REGISTER);
        eventMessage.setFrom(messageUser.getUsername());
        eventMessage.setTo("SERVER");
        eventMessage.setBody(messageUser);
        try {
            return sendSync(eventMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送注册消息
     *
     * @param username
     * @return msgId
     */
    public List<MessageUser> getUserList(String username) {
        EventMessage eventMessage = EventMessage.builderEventType(EventType.GET_USER);
        eventMessage.setFrom(username);
        eventMessage.setTo("SERVER");
        try {
            EventMessage message = sendSync(eventMessage);
            return (List<MessageUser>) message.getBody();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
