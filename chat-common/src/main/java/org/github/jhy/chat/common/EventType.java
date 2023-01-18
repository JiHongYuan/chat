package org.github.jhy.chat.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jihongyuan
 * @date 2023/1/8 10:58
 */
@Getter
@AllArgsConstructor
public enum EventType {

    REGISTER(0, "注册"),
    MESSAGE(1, "消息"),
    CLIENT_ANSWER(10, "客户端回答"),
    SERVER_ANSWER(11, "服务器回答"),
    ;

    private final int type;

    private final String msg;

}
