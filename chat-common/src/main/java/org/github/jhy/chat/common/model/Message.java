package org.github.jhy.chat.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jihongyuan
 * @date 2023/1/8 15:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String code;

    private String msg;

    private Object data;

    public Message(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
