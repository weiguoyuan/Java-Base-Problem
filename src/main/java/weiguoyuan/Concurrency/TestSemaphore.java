package weiguoyuan.Concurrency;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Created by william on 2017/11/3.
 * Semaphore 信号量 用于控制同时访问某个特定资源的操作数量,或者同时执行某个指定操作的数量.
 * 可以用来实现某种资源池,或控制容器的边界.
 */
public class TestSemaphore {

    public static void main(String[] args){
        BoundedHashSet<Thread> set = new BoundedHashSet<Thread>(3);

        for(int i=0;i<10;i++){
            Thread t = new Thread();
            try {
                System.out.println(set.add(t));//只执行3次
                System.out.println(set.getSem().getQueueLength());
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }

}

class BoundedHashSet<T>{
    private final Set<T> set;
    private final Semaphore sem;

    public Semaphore getSem() {
        return sem;
    }


    public BoundedHashSet(int bound){
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException{
        sem.acquire();//获取一个许可(资源)
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        }finally {
            if(!wasAdded)
                sem.release();//释放一个许可
        }
    }

    public boolean remove(Object o){
        boolean wasRemoved = set.remove(o);
        if(wasRemoved){
            sem.release();
        }
        return wasRemoved;
    }

}
