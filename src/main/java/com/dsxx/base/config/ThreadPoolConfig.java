package com.dsxx.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 这个Bean是线程池<br/>
 * 1。通过com.dsxx.base.util.TaskPool类添加的任务会到这个线程池中来执行<br/>
 * 2. 在Service的方法中可以通过@Scheduled @Async这两个注解来实现多线程的定时任务调用<br/>
 *
 * @author slm
 * @date 2018/5/25
 */
@EnableAsync
@Configuration
public class ThreadPoolConfig {

    /**
     * 可以通过配置文件来配置线程池大小，根据业务情况来配置默认是100
     */
    @Value("${taskPool.poolSize:100}")
    private int poolSize;

    @Bean("threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler taskExecutor() {
        // 创建一个线程池对象
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 定义一个线程池大小
        scheduler.setPoolSize(poolSize);
        // 线程池名的前缀
        scheduler.setThreadNamePrefix("my-taskPool-");
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        scheduler.setAwaitTerminationSeconds(60);
        // 线程池对拒绝任务的处理策略,当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；
        // 如果执行程序已关闭，则会丢弃该任务
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return scheduler;
    }
}
