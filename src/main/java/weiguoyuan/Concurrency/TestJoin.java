package weiguoyuan.Concurrency;

/**
 * Created by william on 2017/11/30.
 * test join()
 */
public class TestJoin {
    public static void main(String[] args) throws InterruptedException{
        CountNumber c1 = new CountNumber();
        CountNumber c2 = new CountNumber();

        c1.start();
        c1.join();//主线程和c2线程等待c1线程完成后再执行
        System.out.println("main c2 wait c1 complete");
        c2.start();//c2线程和主线程交替执行
        for(int i=0;i<100;i++){
            Thread.sleep(100);
            System.out.println("main thread running");
        }
    }
}

class CountNumber extends Thread{
    @Override
    public void run(){
        for(int i=0;i<100;i++){
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.getName()+":"+i);
        }
    }
}
