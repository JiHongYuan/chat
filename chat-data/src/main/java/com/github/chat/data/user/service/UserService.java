package com.github.chat.data.user.service;

import com.github.chat.data.user.service.impl.UserServiceImpl;

/**
 * @author jihongyuan
 * @date 2023/2/6 11:12
 */
public interface UserService {

    UserService INSTANT = new UserServiceImpl();

    /**
     * exist username
     *
     * @param username username
     * @return exist
     */
    boolean existUsername(String username);

    /**
     * login
     *
     * @param username username
     * @param password pwd
     */
    void login(String username, String password);

    /**
     * insert user
     *
     * @param username username
     * @param password pwd
     */
    void add(String username, String password);

}
