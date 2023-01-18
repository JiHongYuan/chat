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

    private String username;

    private String password;

}
