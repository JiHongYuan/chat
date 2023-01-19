package org.github.jhy.chat.server.support;

import org.github.jhy.chat.server.IRegister;
import org.github.jhy.chat.server.model.UserSession;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jihongyuan
 * @date 2023/1/8 15:11
 */
public class ChatUserRegister implements IRegister<UserSession> {

    private final ConcurrentHashMap<String, UserSession> userHashMap = new ConcurrentHashMap<>();

    @Override
    public void register(String name, UserSession object) {
        userHashMap.put(name, object);
    }

    @Override
    public void unRegister(String name) {
        userHashMap.remove(name);
    }

    @Override
    public UserSession get(String name) {
        return userHashMap.get(name);
    }

    public Collection<UserSession> getUsers() {
        return userHashMap.values();
    }

}
