package org.github.jhy.chat.client.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;
import org.github.jhy.chat.common.EventMessage;

import java.util.List;

/**
 * @author jihongyuan
 * @date 2023/1/8 11:03
 */
public class EventMessageCodec extends MessageToMessageCodec<ByteBuf, EventMessage> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext ctx, EventMessage msg, List<Object> out) throws Exception {
        if (msg.getBody() != null) {
            msg.setBody(mapper.writeValueAsString(msg.getBody()));
        }
        String str = mapper.writeValueAsString(msg);
        out.add(Unpooled.copiedBuffer(str, CharsetUtil.UTF_8));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        String strMsg = msg.toString(CharsetUtil.UTF_8);
        EventMessage eventMessage = mapper.readValue(strMsg, EventMessage.class);
        out.add(eventMessage);
    }

}
