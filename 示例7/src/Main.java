import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Main {
    // 每一批次要凑够4个线程
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(4,new Action());

    public static void main(String[] args) {
        try {
            // 我们开启4个线程
            for (int i = 0; i < 5; i++) {
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
                Thread.sleep((long) (Math.random() * 100));
                System.out.println(threadName + " 完成第一阶段工作");
                // 到达等待节点
                System.out.println(threadName + "到达，时间是："+ new Date().getTime() +"，栅栏倒计数： " + cyclicBarrier.await());
                System.out.println(threadName + " 进入第二阶段工作");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class Action implements Runnable {
        public void run() {
            try {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " 进行额外任务");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

