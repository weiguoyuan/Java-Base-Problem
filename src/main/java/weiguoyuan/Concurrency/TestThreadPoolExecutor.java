package weiguoyuan.Concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by william on 2017/11/22.
 * 测试Thread pool
 * ThreadPoolExecutor 有一个任务队列 任务很多可以放在队列中等待执行
 */
public class TestThreadPoolExecutor {

    private final static int corePoolSize = 100;
    private final static int maximumPoolSize = 1000;
    private final static long keepAliveTime = 10000;
    private final static BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.MICROSECONDS,workQueue);

    public static void main(String[] args){
        while (true){
            threadPool.execute(new Atomic());
        }
    }
}
