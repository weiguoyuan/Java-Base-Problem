package weiguoyuan.Concurrency;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by william on 2017/11/16.
 * 测试用Interrupt中断 实现线程的退出(通常 中断是实现取消的最合理方式)想要取消某一线程,(主线程)将该线程中断(线程并未退出)
 * 只是发出中断请求,然后由线程在下一个合适的时刻中断自己(这时刻叫取消点) 这样可以直接让线程停止执行
 */
public class TestInterrupt {

    private static final ExecutorService taskExec = Executors.newScheduledThreadPool(10);

    public static void main(String[] args){
        try {
            aSecondOfPrimes();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<BigInteger> aSecondOfPrimes()throws InterruptedException{
        BrokenPrimeProducer generator = new BrokenPrimeProducer();
        new Thread(generator).start();
        try{
            TimeUnit.SECONDS.sleep(1);
        }finally {
            generator.cancel();//直接中断了开启的线程 取消点
        }
        return generator.get();
    }
    //倒计时执行任务 通过Future来取消任务
    public static void timedRun(Runnable r,long timeout,TimeUnit unit)throws InterruptedException{
        Future<?> task = taskExec.submit(r);
        try{
            task.get(timeout,unit);
        }catch (TimeoutException e){
            e.printStackTrace();
            //将任务取消
            task.cancel(true);
        }catch (ExecutionException e){
            e.printStackTrace();
            //如果在任务中出现了异常 重新抛出该异常给主线程处理
            //throw launderThrowable(e.getCause());
        }finally {
            task.cancel(true);//如果任务已经结束 取消该进程也不会带来影响 如果任务还在进行 则中断该进程
        }
    }

}

class BrokenPrimeProducer extends Thread{
    private final BlockingQueue<BigInteger> queue = new LinkedBlockingQueue<BigInteger>();


    public void run(){
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted()){
                queue.put(p=p.nextProbablePrime());
            }
        }catch (InterruptedException e){
            e.printStackTrace();
            /*
            此处处理线程退去 真正取消线程的地方
             */
        }
    }
    public void cancel(){interrupt();}

    public  synchronized List<BigInteger> get(){
        return new ArrayList<BigInteger>(queue);
    }
}
