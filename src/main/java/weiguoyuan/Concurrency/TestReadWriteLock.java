package weiguoyuan.Concurrency;


import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by william on 2017/11/28.
 *
 */
public class TestReadWriteLock {
}

class ReadWriteLockMap<K,V> {
    private final Map<K,V> map;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock r = lock.readLock();
    private final Lock w = lock.writeLock();

    public ReadWriteLockMap(Map<K,V> map){
        this.map = map;
    }

    public V put(K key,V value){
        w.lock();
        try {
            return map.put(key,value);
        }finally {
            w.unlock();
        }
    }

    public V get(Object key){
        r.lock();
        try {
            return map.get(key);
        }finally {
            r.unlock();
        }
    }
}
