import java.util.concurrent.Callable;

class RunnableDemo implements Runnable {
    @Override
    public void run() {
        System.out.println("run function is in Thread :" +  Thread.currentThread().getName());
        for (int i = 0; i < 3; i++) {
            System.out.println("Thread " + Thread.currentThread().getName() + " print : " + i);
        }
        // 该任务的返回值
//        return "result from " + Thread.currentThread().getName();
    }
}