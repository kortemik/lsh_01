package com.teragrep.lsh_01.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class Pool<T extends Poolable> implements AutoCloseable, Supplier<T> {

    private static final Logger LOGGER = LogManager.getLogger(com.teragrep.lsh_01.pool.RelpConnectionPool.class);

    private final Supplier<T> supplier;

    private final ConcurrentLinkedQueue<T> queue;

    private final T stub;

    private final Lock lock = new ReentrantLock();

    private final AtomicBoolean close;

    public Pool(final Supplier<T> supplier, T stub) {
        this.supplier = supplier;
        this.queue = new ConcurrentLinkedQueue<>();
        this.stub = stub;
        this.close = new AtomicBoolean();
    }

    public T get() {
        T object;
        if (close.get()) {
            object = stub;
        }
        else {
            // get or create
            object = queue.poll();
            if (object == null) {
                object = supplier.get();
            }
        }

        return object;
    }

    public void offer(T object) {
        if (!object.isStub()) {
            queue.add(object);
        }

        if (close.get()) {
            while (queue.peek() != null) {
                if (lock.tryLock()) {
                    while (true) {
                        T pooled = queue.poll();
                        if (pooled == null) {
                            break;
                        }
                        else {
                            try {
                                LOGGER.debug("Closing poolable <{}>", pooled);
                                pooled.close();
                                LOGGER.debug("Closed poolable <{}>", pooled);
                            }
                            catch (Exception exception) {
                                LOGGER.warn("Exception <{}> while closing poolable <{}>", exception.getMessage(),
                                        pooled
                                );
                            }
                        }
                    }
                    lock.unlock();
                }
                else {
                    break;
                }
            }
        }
    }

    public void close() {
        close.set(true);

        // close all that are in the pool right now
        offer(stub);
    }

}
