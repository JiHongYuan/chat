package org.github.jhy.chat.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jihongyuan
 * @date 2023/1/18 9:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageUser {

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 状态 */
    private UserStatus status;

    public MessageUser(String username, UserStatus status) {
        this.username = username;
        this.status = status;
    }

    public MessageUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
