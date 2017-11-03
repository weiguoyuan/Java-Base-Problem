package weiguoyuan.Concurrency;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by william on 2017/11/3.
 * Barrier 栅栏 闭锁是等待事件，栅栏是等待其他线程
 *
 *CountDownLatch	                          CyclicBarrier
 *减计数方式	                                  加计数方式
 *计算为0时释放所有等待的线程	                  计数达到指定值时释放所有等待线程
 *计数为0时，无法重置	                          计数达到指定值时，计数置为0重新开始
 *调用countDown()方法计数减一，	              调用await()方法计数加1，若加1后的值不等于构造方法的值，则线程阻塞
 *调用await()方法只进行阻塞，对计数没任何影响
 *不可重复利用	                               可重复利用
 *CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
 *而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；(比如所有人约定6:00在学校集合,到了的人等其他人都到齐了 再去干别的)
 * 另一种栅栏是Exchanger 一种两方(Two-Party)栅栏,各方在栅栏位置交换数据,当两方执行不对称操作时比较有用,比如一个线程往缓冲区写数据,另一个线程从缓冲区读数据
 * 读完后删除数据,线程可以使用Exchanger来汇合,交换缓冲区.
 */
public class TestCyclicBarrier{
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);

        for(int i=0;i<N;i++) {
            new Writer(barrier).start();
        }

        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("CyclicBarrier重用");

        for(int i=0;i<N;i++) {
            new Writer(barrier).start();
        }
    }
    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");

                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"所有线程写入完毕，继续处理其他任务...");
        }
    }
}


