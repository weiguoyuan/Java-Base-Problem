package weiguoyuan.Concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by william on 2017/12/22.
 */
public class TestLockSourceCode {
    public static void main(String[] args){
        TestThreadSource t1 = new TestThreadSource();
        TestThreadSource t2 = new TestThreadSource();
        t1.start();
        t2.start();
        for(int i=0;i<50;i++){
            TestThreadSource t = new TestThreadSource();
            t.start();
        }
    }

}

class TestThreadSource extends Thread{
    private volatile static int count = 5000;
    public void run(){

        Lock lock = new ReentrantLock();

        lock.lock();
        try {
            for(int i = 0;i<count;i++){
                count--;
                System.out.println(Thread.currentThread().getName()+" : "+count);
            }
        }finally {
            lock.unlock();
        }

    }
}
