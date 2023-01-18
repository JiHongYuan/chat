package org.github.jhy.chat.server.support;

import io.netty.channel.Channel;
import org.github.jhy.chat.server.IRegister;

import java.util.concurrent.ConcurrentHashMap;


/**
 * @author jihongyuan
 * @date 2023/1/6 15:21
 */
public class ChannelClientRegister implements IRegister<Channel> {

    private final ConcurrentHashMap<String, Channel> channelHashMap = new ConcurrentHashMap<>();

    @Override
    public void register(String name, Channel object) {
        channelHashMap.put(name, object);
    }

    @Override
    public void unRegister(String name) {
        channelHashMap.remove(name);
    }

    @Override
    public Channel get(String name) {
        return channelHashMap.get(name);
    }

}