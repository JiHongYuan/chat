package org.github.jhy.chat.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.JSON;

import java.util.List;

/**
 * @author jihongyuan
 * @date 2023/1/8 11:03
 */
public class EventMessageCodec extends MessageToMessageCodec<ByteBuf, EventMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, EventMessage msg, List<Object> out) {
        EventMessage rst = msg.copy();
        if (msg.getBody() != null) {
            rst.setBody(JSON.toString(msg.getBody()));
        }
        String str = JSON.toString(rst);
        out.add(Unpooled.copiedBuffer(str, CharsetUtil.UTF_8));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        String strMsg = msg.toString(CharsetUtil.UTF_8);
        EventMessage eventMessage = JSON.parseObject(strMsg, EventMessage.class);
        out.add(eventMessage);
    }

}
