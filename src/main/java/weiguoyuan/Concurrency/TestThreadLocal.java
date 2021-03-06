package weiguoyuan.Concurrency;

/**
 * Created by william on 2017/10/24.
 * 测试ThreadLocal 每个线程都维护一份变量
 */

public class TestThreadLocal {
    private static final ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new MyThread(i)).start();
        }
    }

    static class MyThread implements Runnable {
        private int index;

        public MyThread(int index) {
            this.index = index;
        }

        public void run() {
            System.out.println("Thread" + index + "\'s init value:" + value.get());
            for (int i = 0; i < 10; i++) {
                value.set(value.get() + index);
            }
            System.out.println("Thread" + index + "\'s accumulation value:" + value.get());
        }
    }
}
