package com.github.chat.data.user.service;

import com.github.chat.data.user.service.impl.UserServiceImpl;

/**
 * @author jihongyuan
 * @date 2023/2/6 11:12
 */
public interface UserService {

    UserService INSTANT = new UserServiceImpl();


    boolean existUsername(String username);

    void login(String username, String password);

    void add(String username, String password);

}
