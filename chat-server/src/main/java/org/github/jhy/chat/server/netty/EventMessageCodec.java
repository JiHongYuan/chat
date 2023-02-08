package org.github.jhy.chat.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.JSON;
import org.github.jhy.chat.common.model.Message;
import org.github.jhy.chat.common.model.MessageUser;

import java.util.List;

/**
 * @author jihongyuan
 * @date 2023/1/8 11:03
 */
@Slf4j
public class EventMessageCodec extends MessageToMessageCodec<ByteBuf, EventMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, EventMessage msg, List<Object> out) throws Exception {
        if (log.isDebugEnabled()) {
            log.info("Thread name: {}, encode: {}", Thread.currentThread().getName(), msg);
        }

        if (msg.getBody() != null) {
            msg.setBody(JSON.toString(msg.getBody()));
        }
        String str = JSON.toString(msg);
        out.add(Unpooled.copiedBuffer(str, CharsetUtil.UTF_8));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        String strMsg = msg.toString(CharsetUtil.UTF_8);

        if (log.isDebugEnabled()) {
            log.info("Thread name: {}, decode: {}", Thread.currentThread().getName(), strMsg);
        }

        EventMessage eventMessage = JSON.parseObject(strMsg, EventMessage.class);

        // 根据时间类型, 转换body对象类型
        String body = (String) eventMessage.getBody();
        eventMessage.setBody(switch (eventMessage.getEventType()) {
            case SIGN_IN, SIGN_UP -> JSON.parseObject(body, MessageUser.class);
            case MESSAGE -> JSON.parseObject(body, Message.class);
            case GET_ON_LINE_USER, GET_USER -> body;
        });

        out.add(eventMessage);
    }

}
