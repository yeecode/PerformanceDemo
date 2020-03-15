import java.util.concurrent.Callable;

class Task implements Callable {
    @Override
    public Object call() {
        System.out.println("run function is in Thread :" +  Thread.currentThread().getName());
        for (int i = 0; i < 3; i++) {
            System.out.println("Thread " + Thread.currentThread().getName() + " print : " + i);
        }
        // 该任务的返回值
        return "result from " + Thread.currentThread().getName();
    }
}