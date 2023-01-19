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

    GET_ON_LINE_USER(2, "获取在线用户"),

    GET_USER(3, "获取所有用户"),

    ;

    private final int type;

    private final String msg;

}
