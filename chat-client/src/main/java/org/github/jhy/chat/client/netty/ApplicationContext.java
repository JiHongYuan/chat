package org.github.jhy.chat.client.netty;

import com.google.common.cache.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.App;
import org.github.jhy.chat.common.EventMessage;
import org.github.jhy.chat.common.model.MessageUser;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author jihongyuan
 * @date 2023/1/17 15:58
 */
@Slf4j
public class ApplicationContext {

    @Getter
    @Setter
    private static App app;

    @Getter
    private static NettyChatClient client;

    @Getter
    @Setter
    private static MessageUser user;

    public static LoadingCache<String, SyncFuture<EventMessage>> futureCache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .maximumSize(10000)
            .concurrencyLevel(20)
            // expireAfterWrite设置写缓存后8秒钟过期
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build(new CacheLoader<>() {
                @Override
                public SyncFuture<EventMessage> load(String key) throws Exception {
                    return null;
                }
            });

    public static LinkedBlockingQueue<EventMessage> messageQueue = new LinkedBlockingQueue<>();

    public static synchronized NettyChatClient initClient() {
        if (client == null) {
            client = new NettyChatClient("127.0.0.1", 9999);
            try {
                client.run();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return client;
    }

}
