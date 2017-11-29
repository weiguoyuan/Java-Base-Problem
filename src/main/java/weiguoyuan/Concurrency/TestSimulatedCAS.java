package weiguoyuan.Concurrency;

import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;

/**
 * Created by william on 2017/11/29.
 * CAS(在大多数处理器架构中实现的比较交换指令) CAS包括3个操作数-需要读写的内存位置V 进行比较的值A 要更新为值B
 * 当V==A CAS才会通过原子方式用新值B更新V并返回V的值 否则不修改返回V的值 (和数据库版本号的乐观锁相似)
 * 多个线程尝试使用CAS同时更新一个变量时 只有一个能更新变量的值 其他的线程都将失败 虽然失败但不会像锁或同步代码块会阻塞线程
 * 线程执行失败后 可根据业务 返回错误提示等
 * CAS的典型使用模式:首先从V中读取值A,并根据A计算新值B,然后再通过CAS以原子方式将V中值由A变成B(只要在这期间没有任何线程将V的值修改为其他的值)
 * 由于CAS能检测来自其他线程的干扰,所以CAS能不使用(阻塞)锁实现原子的读-改-写操作
 */
@ThreadSafe
public class TestSimulatedCAS {
    @GuardedBy("this") private int value;

    public synchronized int get(){return value;}//加锁模拟CAS

    public synchronized int compareAndSwap(int expectedValue,int newValue){
        int oldValue = value;
        if(oldValue ==expectedValue){
            value = newValue;
        }
        return oldValue;
    }

    public synchronized boolean compareAndSet(int expectedValue,int newValue){
        return (expectedValue == compareAndSwap(expectedValue,newValue));
    }
}
