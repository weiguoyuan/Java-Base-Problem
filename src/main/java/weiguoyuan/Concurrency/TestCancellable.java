package weiguoyuan.Concurrency;

import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by william on 2017/11/15.
 * 测试线程的取消 输出1以后的素数 1s后终止(确切的说是1s多 while的下次判断也需要时间)
 */

public class TestCancellable{

    public static void main(String[] args){
        try {
            aSecondOfPrimes();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<BigInteger> aSecondOfPrimes()throws InterruptedException{
        PrimeGenerator generator = new PrimeGenerator();
        new Thread(generator).start();
        try{
            TimeUnit.SECONDS.sleep(1);
        }finally {
            generator.cancel();
        }
        return generator.get();
    }
}
@ThreadSafe
class PrimeGenerator implements Runnable {
    @GuardedBy("this")
    private final List<BigInteger> primes = new ArrayList<BigInteger>();
    private volatile boolean cancelled;

    public void run(){
        BigInteger p = BigInteger.ONE;
        while (!cancelled){//判断是否取消
            p = p.nextProbablePrime();
            System.out.println(p);
            synchronized (this){
                primes.add(p);
            }
        }
    }

    public void cancel(){
        cancelled = true;
    }

    public  synchronized List<BigInteger> get(){
        return new ArrayList<BigInteger>(primes);
    }

}
