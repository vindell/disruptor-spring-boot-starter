package com.lmax.disruptor.spring.boot.thread;

import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.function.BiFunction;

public enum DisruptorThreadFactory implements ThreadFactory {

    DEFAULT_THREAD_FACTORY(true, (daemon, r) -> {
        Thread t = new Thread(r);
        t.setDaemon(daemon);
        t.setName("Disruptor-Thread");
        return t;
    }),
    LOGGER_THREAD_FACTORY(true, (daemon, r) -> {
        Thread t = new Thread(r);
        t.setDaemon(daemon);
        t.setName("Disruptor-Thread");
        t.setUncaughtExceptionHandler((t1, e) -> LoggerFactory.getLogger(t1.getName()).error(e.getMessage(), e));
        return t;
    }),
    MAX_PRIORITY_THREAD_FACTORY(true, (daemon, r) -> {
        Thread t = new Thread(r);
        t.setDaemon(daemon);
        t.setName("Disruptor-Max-Priority-Thread");
        t.setPriority(Thread.MAX_PRIORITY);
        return t;
    }),

    ;

    private boolean daemon = false;
    private BiFunction<Boolean, Runnable, Thread> function;


    DisruptorThreadFactory(BiFunction<Boolean, Runnable, Thread> function){
        this.function = function;
    }

    DisruptorThreadFactory(boolean daemon, BiFunction<Boolean, Runnable, Thread> function){
        this.daemon = daemon;
        this.function = function;
    }

    @Override
    public Thread newThread(Runnable r) {
        return function.apply(daemon, r);
    }

}
