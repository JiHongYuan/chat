package org.github.jhy.chat.client.netty;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author jihongyuan
 * @date 2023/1/18 10:47
 */
public class SyncFuture<T> implements Future<T> {

    private final CountDownLatch latch = new CountDownLatch(1);

    private T response;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return response != null;
    }

    @Override
    public T get() throws InterruptedException {
        if (latch.await(15, TimeUnit.SECONDS)) {
            return this.response;
        }
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException {
        if (latch.await(timeout, unit)) {
            return this.response;
        }
        return null;
    }

    public void setResponse(T response) {
        this.response = response;
        latch.countDown();
    }

}
