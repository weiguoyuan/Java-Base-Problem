package weiguoyuan.Concurrency;

import javax.naming.InsufficientResourcesException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by william on 2017/11/28.
 * Lock(显示锁)提供了无条件的 可轮询的 定时的可中断的锁 synchronized(内置锁 必须在获取该锁的代码块中释放 无法实现非阻塞结构的锁)
 */
public class TestLock {
    public static void main(String[] args){

    }

    public boolean transferMoney(Account from, Account to, Double amount, long timeout, TimeUnit unit) throws InsufficientResourcesException,InterruptedException{
        long stopTime = System.nanoTime() + unit.toNanos(timeout);
        //long fixedDelay = getFixedDelayComponentNanos(timeout,unit);
        //long randMod = getRandomDelayModulusNanosss(timeout,unit);
        while (true){
            if(from.lock.tryLock()){//如果不能同时获得2个锁 那么就回退重新尝试
                try {
                    if(to.lock.tryLock()){//可定时(加时间参数)可轮询的实现 可以防止死锁
                        try {
                            if((from.getBalance()-amount)<0)
                                throw new InsufficientResourcesException();
                            else {
                                from.debit(amount);
                                to.credit(amount);
                                return true;
                            }
                        }finally {
                            to.lock.unlock();//一定要记住释放锁
                        }
                    }
                }finally {
                    from.lock.unlock();
                }
            }
            if(System.nanoTime()<stopTime){
                return false;
            }
            //unit.NANOSECONDS.sleep(fixedDelay + rnd.nestLong()%randMod); 休眠时间包含固定部分和随机部分来降低发生活锁的几率
            unit.NANOSECONDS.sleep(10+10);
        }
    }
}

class Account{

    private Double balance;

    public Lock lock = new ReentrantLock();

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void debit(double amount){
        lock.lock();
        try {
            balance = this.balance - amount;
        }finally {
            lock.unlock();
        }
    }

    public void credit(double amount){
        lock.lock();
        try {
            balance = this.balance + amount;
        }finally {
            lock.unlock();
        }
    }

}
