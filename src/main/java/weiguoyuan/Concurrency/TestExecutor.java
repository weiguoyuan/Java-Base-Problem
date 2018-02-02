package weiguoyuan.Concurrency;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by william on 2017/11/14.
 * 将任务的提交和执行解耦 可以用Executor来替代Thread来获得更灵活的执行策略
 */
public class TestExecutor {
    private static final int NTHREADS = 100;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);//100的固定大小线程池

    public static void main(String[] args) throws Exception{
          while (true){
              exec.execute(new Atomic());//线程池来执行
          }
    }
}

class ThreadPerTaskExecutor implements Executor{

    @Override
    public void execute(Runnable command) {
        new Thread(command).run();//为每个任务执行启动一个新的线程来执行
    }

}

class WithinThreadExecutor implements Executor{

    @Override
    public void execute(Runnable command) {
        command.run();//用一个线程来执行所有任务同步方式
    }
}
