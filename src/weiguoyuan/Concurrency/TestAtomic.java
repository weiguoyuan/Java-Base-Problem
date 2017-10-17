package weiguoyuan.Concurrency;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by william on 2017/10/16.
 */

class Atomic implements Runnable{

    private final AtomicLong count = new AtomicLong(1000);

    @Override
    public void run() {

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

        }
    }
}

