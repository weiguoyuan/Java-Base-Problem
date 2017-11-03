package weiguoyuan.Concurrency;

import java.util.concurrent.CountDownLatch;

/**
 * Created by william on 2017/11/2.
 * CountDownLatch是一种灵活的闭锁实现,可以使多个线程等待一组事件发生,闭锁状态包含一个计数器,该计数器被初始化为一个正数,表示需要等待的事件数量,
 * countDown方法递减计数器,表示一个事件已经发生了,而await方法等待计数器达到0,这表示所有需要等待的时间都已经发生.如果计数器的值非0,那么await
 * 会一直阻塞直到计数器为0,或者等待中的线程中断,或者超时.
 * 闭锁:一种同步工具类,当闭锁达到状态前,任何线程都处于等待,当达到状态所有线程执行,用于确保某些活动直到其它活动都执行完成才继续执行.
 * 这个例子是创建一定数量的线程利用线程并发的执行指定任务.StartingGate初始值是1,EndingGate是工作线程的数量,线程创建后等待所有线程创建完成,
 * 完成后线程开始执行调用EndingGate的countDown方法-1,线程开始执行,直到执行完成统计消耗的时间.
 */
public class TestCountDownLatch {

    public static long timeTasks(int nThreads,final Runnable task)throws InterruptedException{
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0;i < nThreads;i++){
            System.out.println("new Thread"+i);
            Thread t = new Thread(){
                public void run(){
                    try {
                        startGate.await();//线程创建开启后等待其他线程开启完成
                        try {
                            task.run();//线程开始执行任务
                        }finally {
                            endGate.countDown();//每完成一个线程任务锁-1 知道为0 后统计时间
                        }
                    }catch (InterruptedException ignored){}
                }
            };
            t.start();//开启线程
        }
        long start = System.nanoTime();
        startGate.countDown();//所有线程开启后 锁-1 为0后执行线程任务
        System.out.println("gate open");
        endGate.await();//等待所有线程执行完成后统计时间
        long end = System.nanoTime();
        return end - start;
    }

    public static void main(String[] args){
        try{
            System.out.println(
                    timeTasks(10,new Atomic())
            );
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
