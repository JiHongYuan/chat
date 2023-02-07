package com.github.chat.data.user.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 用户
 * @author jihongyuan
 * @date 2023/2/6 11:13
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /** ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 用户状态(1:正常) */
    private String status;

    private boolean isDeleted;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}
