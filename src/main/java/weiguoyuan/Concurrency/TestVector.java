package weiguoyuan.Concurrency;

import java.util.Vector;

/**
 * Created by william on 2017/10/30.
 */
public class TestVector {

    public static void main(String[] args){
        UnSafeVector unSafeVector = new UnSafeVector();
        new Thread(unSafeVector).start();
        new Thread(unSafeVector).start();
    }
}

class UnSafeVector implements Runnable{
    private static Vector vector = new Vector(5);
    public UnSafeVector(){
        vector.add("dd");
        vector.add("dd");
        vector.add("dd");
        vector.add("dd");
        vector.add("dd");
    }
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName()+":-"+vector.get(vector.size()-1));
        int size = vector.size();
        Thread.yield();
        System.out.println(Thread.currentThread().getName()+":-"+vector.size());
        System.out.println(Thread.currentThread().getName()+":-"+vector.get(size-1));
        vector.remove(size-1);
        System.out.println(Thread.currentThread().getName()+":-"+vector.size());
    }
}
