import java.util.concurrent.FutureTask;

public class Main {
    public static void main(String[] args) {
        System.out.println("main function is in Thread :" + Thread.currentThread().getName());
        // 使用FutureTask包装任务
        FutureTask futureTask = new FutureTask(new Task());
        // 创建一个线程并将任务载入
        Thread thread = new Thread(futureTask);
        // 启动新线程
        thread.start();
        for (int i = 0; i < 3; i++) {
            System.out.println("Thread " + Thread.currentThread().getName() + " print : " + i);
        }
        // 获取新线程的返回值
        try {
            System.out.println("return value form new thread : " + futureTask.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}