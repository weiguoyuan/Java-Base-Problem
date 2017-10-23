package weiguoyuan.Concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by william on 2017/10/20.
 * 测试导致多线程程序出错的问题,对象的共享问题内存可见性(Memory Visibility),Reordering问题(重排序).
 * 在没有同步的情况下,编译器,处理器以及运行时等都可能对操作的执行顺序进行调整,在缺乏同步的多线程程序中,无法对内存操作的执行顺序进行判断.
 * 只开1个线程的情况下,变量不加volatile标识,输出的number可能出现0(失效数据)而不是43,有volatile不会出现0的情况
 * 变量加volatile标识,但开启3个线程出现了输出的number可能出现0的情况 加锁机制可确保可见性和原子性(++) volatile只能确保可见性
 */
public class NoVisibility {
    private volatile static boolean ready;//volatile 用来确保变量的更新通知到其他线程 编译器,处理器以及运行时都会注意到这个变量是共享的
    private volatile static int number;//不会将该变量上的操作与其他的其他内存一起重排 不会缓存在寄存器或者处理器不可见的地方

    private static class ReaderThread extends Thread {
        public void run(){
            System.out.println(Thread.currentThread().getName());
            while (!ready){
                Thread.yield();
                System.out.println(Thread.currentThread().getName()+":"+number);
            }
        }
    }

    public static void main(String[] args){

        Lock lock =new ReentrantLock();

        lock.lock();{

        }
        new ReaderThread().start();
        new ReaderThread().start();
        new ReaderThread().start();
        System.out.println("begin evaluation");
        number = 43;
        ready = true;
    }
}
