package com.lmax.disruptor.spring.boot.thread;

import com.lmax.disruptor.*;

import java.util.function.Function;

/**
 * 决定一个消费者将如何等待生产者将Event置入Disruptor的策略。用来权衡当生产者无法将新的事件放进RingBuffer时的处理策略。
 * （例如：当生产者太快，消费者太慢，会导致生成者获取不到新的事件槽来插入新事件，则会根据该策略进行处理，默认会堵塞）
 */
public enum DisruptorWaitStrategy {

    /**
     * BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现
     */
    BLOCKING_WAIT((x) -> new BlockingWaitStrategy()),

    /**
     * SleepingWaitStrategy 的性能表现跟BlockingWaitStrategy差不多，对CPU的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景
     */
    SLEEPING_WAIT((x) -> new SleepingWaitStrategy()),

    /**
     * YieldingWaitStrategy是可以被用在低延迟系统中的两个策略之一，这种策略在减低系统延迟的同时也会增加CPU运算量。YieldingWaitStrategy策略会循环等待sequence增加到合适的值。循环中调用Thread.yield()允许其他准备好的线程执行。如果需要高性能而且事件消费者线程比逻辑内核少的时候，推荐使用YieldingWaitStrategy策略。例如：在开启超线程的时候。
     */
    YIELDING_WAIT((x) -> new YieldingWaitStrategy()),

    /**
     * BusySpinWaitStrategy是性能最高的等待策略，同时也是对部署环境要求最高的策略。这个性能最好用在事件处理线程比物理内核数目还要小的时候。例如：在禁用超线程技术的时候。
     */
    BUSYSPIN_WAIT((x) -> new BusySpinWaitStrategy());

    Function<Integer, com.lmax.disruptor.WaitStrategy> function;

    DisruptorWaitStrategy(Function<Integer, com.lmax.disruptor.WaitStrategy> function){
        this.function = function;
    }

    public static DisruptorWaitStrategy from(String name) {
        for (DisruptorWaitStrategy strategy : DisruptorWaitStrategy.values()) {
            if (strategy.name().equalsIgnoreCase(name)) {
                return strategy;
            }
        }
        return null;
    }

    public WaitStrategy get() {
        return function.apply(0);
    }

}
