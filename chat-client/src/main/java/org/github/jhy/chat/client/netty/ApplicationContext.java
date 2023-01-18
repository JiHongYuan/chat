package org.github.jhy.chat.client.netty;

import com.google.common.cache.*;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.github.jhy.chat.client.App;
import org.github.jhy.chat.common.EventMessage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author jihongyuan
 * @date 2023/1/17 15:58
 */
@Slf4j
public class ApplicationContext {

    private static App app;

    private static NettyChatClient client;

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


    public static App getApp() {
        return app;
    }

    public static void setApp(App app) {
        ApplicationContext.app = app;
    }

    public static NettyChatClient getClient() {
        return client;
    }

    public static void setClient(NettyChatClient client) {
        ApplicationContext.client = client;
    }

}
