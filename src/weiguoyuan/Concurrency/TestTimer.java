package weiguoyuan.Concurrency;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by william on 2017/11/13.
 * Timer类负责管理延迟任务("在100ms后执行") 周期任务("每10ms执行一次该任务")
 * 但Timer执行所有定时任务的时候只会创建一个线程 如果一个任务执行的时间过长会影响其他任务执行的时间
 * 任务一:10ms执行一次,任务二:执行需要40ms 如果任务二执行时 这40ms内 任务一就没法执行了
 * Timer不捕获TimeTask(任务)的异常,TimeTask抛出未检查的异常会导致定时线程终止 Timer也不会恢复线程执行
 * 一般用ScheduledThreadPoolExecutor替换Timer使用,ScheduledThreadPoolExecutor
 */
public class TestTimer {
    public static void main(String[] args) throws Exception{
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(),1);
        TimeUnit.SECONDS.sleep(1);
        timer.schedule(new ThrowTask(),1);
        TimeUnit.SECONDS.sleep(5);
    }

    static class ThrowTask extends TimerTask{
        public void run(){
            System.out.println("time task");
            throw new RuntimeException();
        }
    }
}
