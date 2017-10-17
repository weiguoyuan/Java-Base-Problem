package weiguoyuan.Concurrency;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by william on 2017/10/16.
 */

class Atomic extends Thread{

    private final AtomicLong count = new AtomicLong(0);

    public void incr(){
        count.incrementAndGet();
    }

    public void run(){
        int i = 1000;
        while (i>0){
            incr();
            System.out.println(count);
            i--;
        }
    }

    public static class TestAtomic {

        public static void main(String[] args){
            Atomic t1 = new Atomic();
            Atomic t2 = new Atomic();
            t1.start();
            t2.start();
        }
    }
}

