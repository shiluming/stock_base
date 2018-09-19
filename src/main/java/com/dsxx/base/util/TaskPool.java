package com.dsxx.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 这个Bean可以手动向线程池中添加一次性定时任务或者周期任务
 *
 * @author slm
 * @date 2017/9/20
 */
@Component
public class TaskPool implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPool.class);
    private static final long serialVersionUID = -3858817618975953839L;
    /**
     * 用来保存每个任务
     * key 为用来获取对应的任务
     */
    private Map<String, ScheduledFuture> tasks = new ConcurrentHashMap<>();


    /**
     * 线程池任务调度类，能够开启线程池进行任务调度。
     * ThreadPoolTaskScheduler.schedule()方法会创建一个定时计划ScheduledFuture，
     * 在这个方法需要添加两个参数，Runnable（线程接口类） 和CronTrigger（定时任务触发器），
     * 在ScheduledFuture中有一个cancel可以停止定时任务。
     */
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    public TaskPool(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    /**
     * 获取每个Id对应的任务
     *
     * @return
     */
    public ScheduledFuture getTask(String id) {
        return tasks.get(id);
    }

    /**
     * 添加一个定时启动的一次性任务
     *
     * @param id
     * @param task
     * @param startTime
     */
    public void addTask(String id, Runnable task, Date startTime) {
        ScheduledFuture sf = threadPoolTaskScheduler.schedule(task, startTime);
        tasks.put(id, sf);
    }

    /**
     * 添加一个任务，cron表达式描述任务周期
     *
     * @param id
     * @param task
     * @param cron
     */
    public void addTask(String id, Runnable task, String cron) {
        ScheduledFuture sf = threadPoolTaskScheduler.schedule(task, new CronTrigger(cron));
        tasks.put(id, sf);
    }

    /**
     * 执行一个任务
     * @param task 任务
     */
    public void addTask(Runnable task){
        threadPoolTaskScheduler.execute(task);
    }

    /**
     * 1分钟清理一次tasks 这个Map，防止内存溢出
     * @author slm
     * @date 2018/05/25
     */
    @Scheduled(fixedDelay = 1 * 60 * 1000)
    @Async
    public void cleanTasksMap() {
        int count = 0;
        Iterator<Map.Entry<String, ScheduledFuture>> iterator = tasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ScheduledFuture> entry = iterator.next();
            ScheduledFuture sf = entry.getValue();
            if (sf == null || sf.isDone() || sf.isCancelled()) {
                iterator.remove();
                count++;
            }
        }
        if (count > 0){
            LOGGER.info("本次清理了{}个ScheduledFuture", count);
        }
    }


}
