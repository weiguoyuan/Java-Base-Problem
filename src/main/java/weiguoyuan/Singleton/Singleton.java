package weiguoyuan.Singleton;

import org.apache.http.annotation.ThreadSafe;

/**
 * Created by william on 2017/12/25.
 * 正确的单例和错误的单例
 */
public class Singleton {
}

class WrongSingleton {

    private static volatile Resource resource = null;

    public static Resource getInstance() {
        if (resource == null) { //volatile保证变量对线程的可见性，但不保证原子性。并发下会多次执行新建对象
            resource = new Resource();
        }
        return resource;
    }
}

class WrongSingleton2 {//双重检查加锁(DCL) 也是错误的 当缺少Happens-Before关系时,就可能出现重排序问题,
                       //这就解释了为什么没有充分同步的情况下发布一个对象会导致另一个线程看到一个只部分构造的对象
    private static Resource resource = null;

    public static Resource getInstance() {//这么设计当初是为了解决两个问题 1无竞争同步的执行速度很慢(synchronized锁当时没有偏向锁和轻量锁) 2JVM启动很慢(懒加载写法)
        if (resource == null) { //有可能其他线程新建了对象 resource的地址不为空 但是这个对象还没有构造完成 应该用volatile 标注resource
            synchronized (WrongSingleton.class) {
                if (resource == null)
                    resource = new Resource();
            }
        }
        return resource;
    }
}

@ThreadSafe
class SafeLazyInitialization {

    private static Resource resource = null;

    public synchronized static Resource getInstance() {
        if (resource == null)
            resource = new Resource();
        return resource;
    }
}

@ThreadSafe
class EagerInitialization {//提前初始化

    private static Resource resource = new Resource();

    public static Resource getResource() { return resource; }
}

@ThreadSafe
class ResourceFactory {//延迟初始化占位类模式 推荐
    private static class ResourceHolder {//内部类用法
        public static Resource resource = new Resource();
    }
    public static Resource getResource() {
        return ResourceHolder.resource ;
    }
}


