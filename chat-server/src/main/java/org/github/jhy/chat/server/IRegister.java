package org.github.jhy.chat.server;

/**
 * @author jihongyuan
 * @date 2023/1/6 15:22
 */
public interface IRegister<T> {

    void register(String name, T object);

    void unRegister(String name);

    T get(String name);

}
