package org.github.jhy.chat.server.model;

import lombok.Data;
import org.github.jhy.chat.common.model.UserStatus;

/**
 * @author jihongyuan
 * @date 2023/1/9 19:22
 */
@Data
public class UserSession {

    /** 用户名 */
    private String username;

    /** sessionId */
    private String sessionId;

    /** 状态 */
    private UserStatus status;

}
