import java.util.concurrent.CountDownLatch;

public class Main {
    // 设置CountDownLatch的计数阈值为3
    private static CountDownLatch countDownLatch = new CountDownLatch(3);

    public static void main(String[] args) {
        try {
            // 启动需要被唤醒的线程
            Thread postTaskThread = new Thread(new PostTask());
            postTaskThread.start();
            // 启动三个前置线程
            for (int i = 0; i < 3; i++) {
                Thread thread = new Thread(new Task());
                thread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class Task implements Runnable {
        public void run() {
            try {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " starts running.");
                // 睡眠一个随机的时间，模拟线程的执行时间差异
                Thread.sleep((long) (Math.random() * 100));
                countDownLatch.countDown();
                System.out.println(threadName + " completed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class PostTask implements Runnable {
        public void run() {
            try {
                String threadName = Thread.currentThread().getName();
                // 挂起，并等待唤起
                countDownLatch.await();
                // 只有当线程重新被countDownLatch唤起后，才能执行下面的语句
                System.out.println(threadName + " is activated.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

