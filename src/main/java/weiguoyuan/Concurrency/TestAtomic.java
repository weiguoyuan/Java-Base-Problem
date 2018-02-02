package weiguoyuan.Concurrency;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by william on 2017/10/16.
 * 测试导致多线程出错的问题,数据竞争和竞争条件
 * 测试运算的原子性
 * 数据竞争和竞争条件的关系 http://www.jianshu.com/p/dd82d070e5c1
 * java.util.concurrent.atomic可以解决原子性问题(数据竞争问题),如i=i+1(incrmentAndGet()替代);的操作,
 * 但竞态条件问题还是要锁来解决.
 */

class Atomic implements Runnable{

    private static final AtomicLong count = new AtomicLong(1000);//java的原子运算

    public synchronized void run() {

        while (count.get() > 0){
            try {
                Thread.sleep(30);
            }catch (Exception e){
                e.printStackTrace();
            }
            synchronized(Atomic.class){

                count.decrementAndGet();

                System.out.println(Thread.currentThread().getName()+":"+count);

            }
        }
    }

    public static class TestAtomic {

        public static void main(String[] args){

            Atomic t1 = new Atomic();

            new Thread(t1).start();
            new Thread(t1).start();
            new Thread(t1).start();

            //test user username

        }
    }

}

