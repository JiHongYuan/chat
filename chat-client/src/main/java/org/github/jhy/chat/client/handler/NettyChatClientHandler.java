package org.github.jhy.chat.client.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.LoadingCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.netty.ApplicationContext;
import org.github.jhy.chat.client.netty.SyncFuture;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.EventType;
import org.github.jhy.chat.common.model.Message;
import org.github.jhy.chat.common.model.MessageUser;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * 聊天室处理类
 */
@Slf4j
public class NettyChatClientHandler extends SimpleChannelInboundHandler<EventMessage> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EventMessage msg) throws Exception {
        EventType eventType = msg.getEventType();

        if (msg.getBody() != null) {
            msg.setBody(switch (eventType) {
                case MESSAGE, SIGN_IN, SIGN_UP -> mapper.readValue(msg.getBody().toString(), Message.class);
                case GET_ON_LINE_USER, GET_USER -> mapper.readerForListOf(MessageUser.class).readValue(msg.getBody().toString());
            });
        }

        if (EventType.MESSAGE == eventType) {
            Message message = (Message) msg.getBody();
            log.info("来自{}的消息: {}", msg.getFrom(), message.getMsg());

            LinkedBlockingQueue<EventMessage> messageQueue = ApplicationContext.messageQueue;
            messageQueue.add(msg);
        }

        LoadingCache<String, SyncFuture<EventMessage>> futureCache = ApplicationContext.futureCache;
        SyncFuture<EventMessage> future = futureCache.get(msg.getMsgId());
        if(future != null){
            future.setResponse(msg);
        }

        if (log.isDebugEnabled()) {
            log.debug(msg.toString());
        }
    }

}
