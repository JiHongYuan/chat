package org.github.jhy.chat.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author jihongyuan
 * @date 2023/1/8 10:56
 */
@Data
@Builder
@AllArgsConstructor
public class EventMessage {

    /** 消息ID */
    private String msgId;

    /** 发送者 */
    private String from;

    /** 接收者 */
    private String to;

    /** 事件 */
    private EventType eventType;

    /** 主体 */
    private Object body;

    /** 发送时间 */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sendTime;

    public EventMessage() {
        this.sendTime = LocalDateTime.now();
    }

    public static EventMessage builderEventType(EventType eventType) {
        return builderEventType(UUID.randomUUID().toString(), eventType);
    }

    public static EventMessage builderEventType(String msgId, EventType eventType) {
        return EventMessage.builder()
                .msgId(msgId)
                .eventType(eventType)
                .sendTime(LocalDateTime.now())
                .build();
    }

}