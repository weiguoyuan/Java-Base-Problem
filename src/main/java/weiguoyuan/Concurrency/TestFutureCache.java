package weiguoyuan.Concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by william on 2017/11/8.
 * 构建一个多线程访问安全且效率高的缓存 存储A(参数)-V(值)对 compute计算传入的A将计算的结果返回并存入cache(HashMap)中
 * 用ConcurrentHashMap替代HashMap
 */
public class TestFutureCache {
}

class Memoizer<A,V> implements Computable<A,V>{

    private final ConcurrentHashMap<A,Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A,V> c;

    public Memoizer(Computable<A,V> c){this.c = c;}

    public V compute(final A arg)throws InterruptedException{
        while (true){
            Future<V> f = cache.get(arg);
            if(f == null){
                Callable<V> eval = new Callable<V>() {//和Runnable接口并列区别是有返回值 通过Future可以获得其返回值(异步执行任务)
                    @Override
                    public V call() throws InterruptedException {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<V>(eval);//FutureTask实现了Runnable和Future<V>接口 它既可以作为Runnable被线程执行，又可以作为Future得到Callable的返回值，那么这个组合的使用有什么好处呢？假设有一个很耗时的返回值需要计算，并且这个返回值不是立刻需要的话，那么就可以使用这个组合，用另一个线程去计算返回值，而当前线程在使用这个返回值之前可以做其它的操作，等到需要这个返回值时，再通过Future得到
                f = cache.putIfAbsent(arg,ft);//将结果存入缓存中
                if(f == null){
                    f = ft;
                    ft.run();//这里调用c.compute 调用了Callable的call()
                }
            }

            try {
                return f.get();//如果cache中有值即是cache中的值 否则获得Callable call()计算的结果 当FutureTask处于未启动或已启动状态时，如果此时我们执行FutureTask.get()方法将导致调用线程阻塞(主线程阻塞等待执行计算的线程返回结果);
                               // 当FutureTask处于已完成状态时，执行FutureTask.get()方法将导致调用线程立即返回结果或者抛出异常。
            }catch (CancellationException e){
                cache.remove(arg,f);
            }catch (ExecutionException e){
                e.getCause().printStackTrace();
            }
        }
    }
}

interface Computable<A,V>{
    V compute(A arg) throws InterruptedException;
}
