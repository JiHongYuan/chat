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

    SIGN_IN(0, "登录"),

    SIGN_UP(1, "注册"),

    MESSAGE(10, "消息"),

    GET_ON_LINE_USER(20, "获取在线用户"),

    GET_USER(21, "获取所有用户"),

    ;

    private final int type;

    private final String msg;

}
