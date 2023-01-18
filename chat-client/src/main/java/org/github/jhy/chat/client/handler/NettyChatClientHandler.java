package org.github.jhy.chat.client.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.LoadingCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.netty.ApplicationContext;
import org.github.jhy.chat.client.netty.SyncFuture;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.EventType;
import org.github.jhy.chat.common.model.Message;


/**
 * 聊天室处理类
 */
@Slf4j
public class NettyChatClientHandler extends SimpleChannelInboundHandler<EventMessage> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EventMessage msg) throws Exception {
        EventType eventType = msg.getEventType();
        if (EventType.MESSAGE == eventType) {
            Message message = mapper.readValue(msg.getBody().toString(), Message.class);
            log.info("来自{}的消息: {}", msg.getTo(), message.getMsg());

        } else {
            LoadingCache<String, SyncFuture<EventMessage>> futureCache = ApplicationContext.futureCache;
            SyncFuture<EventMessage> future = futureCache.get(msg.getMsgId());
            future.setResponse(msg);
        }

        if (log.isDebugEnabled()) {
            log.debug(msg.toString());
        }
    }

}
