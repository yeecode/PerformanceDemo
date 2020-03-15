import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        // 创建一个具有3个线程的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        // 向线程池中提交任务
        Future futureOfRunnableDemo = executorService.submit(new RunnableDemo());
        Future futureOfCallableDemo = executorService.submit(new CallableDemo());
        // 关闭线程池
        executorService.shutdown();
        // 获取任务结果
        try {
            System.out.println("result of RunnableDemo :" + futureOfRunnableDemo.get());
            System.out.println("result of CallableDemo :" + futureOfCallableDemo.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
