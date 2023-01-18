package org.github.jhy.chat.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.common.EventMessage;

import java.util.List;

/**
 * @author jihongyuan
 * @date 2023/1/8 11:03
 */
@Slf4j
public class EventMessageCodec extends MessageToMessageCodec<ByteBuf, EventMessage> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, EventMessage msg, List<Object> out) throws Exception {
        if (log.isDebugEnabled()) {
            log.info("Thread id: {}, encode: {}", Thread.currentThread().getId(), msg);
        }
        String str = mapper.writeValueAsString(msg);
        out.add(Unpooled.copiedBuffer(str, CharsetUtil.UTF_8));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        String strMsg = msg.toString(CharsetUtil.UTF_8);

        if (log.isDebugEnabled()) {
            log.info("Thread id: {}, decode: {}", Thread.currentThread().getId(), strMsg);
        }

        EventMessage eventMessage = mapper.readValue(strMsg, EventMessage.class);
        out.add(eventMessage);
    }

}
