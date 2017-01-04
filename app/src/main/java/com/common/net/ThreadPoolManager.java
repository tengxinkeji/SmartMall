package com.common.net;

import com.common.app.degexce.L;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/7/26.
 */
public class ThreadPoolManager {
    private ExecutorService executorService;

    public ThreadPoolManager() {
        int num=Runtime.getRuntime().availableProcessors();
        executorService= Executors.newFixedThreadPool(num);
    }
    public static final ThreadPoolManager manager=new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return manager;
    }

    public void addTask(Runnable runnable) {
        executorService.execute(runnable);
    }

  /*  public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(3,                 //core pool size
                Integer.MAX_VALUE, //maximum pool size
                60L,               //keep alive time
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public void run(){
//         线程池的大小会根据执行的任务数动态分配
//        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        ExecutorService cachedThreadPool = newCachedThreadPool();
//
//        // 创建可以容纳3个线程的线程池
//        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
//
//
//
//        // 创建单个线程的线程池，如果当前线程在执行任务时突然中断，则会创建一个新的线程替代它继续执行任务
//        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
//
//        // 效果类似于Timer定时器
//        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);

        run(cachedThreadPool);
    }

    private  void run(ExecutorService threadPool) {

        for (Map.Entry<Integer, RequestParams> entry:urlList.entrySet()){
            int taskID = entry.getKey();
            RequestParams urlModel=entry.getValue();
            threadPool.execute(new URLRequest(urlModel,0, mHandler, taskID));

            L.MyLog("MyExecutor--->url:", taskID + "---" + urlModel.toString());
        }

        threadPool.shutdown();// 任务执行完毕，关闭线程池

    }*/
}
