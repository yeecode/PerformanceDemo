import java.util.concurrent.Callable;

class CallableDemo implements Callable {
    @Override
    public Object call() {
        System.out.println("call function is in Thread :" +  Thread.currentThread().getName());
        for (int i = 0; i < 3; i++) {
            System.out.println("Thread " + Thread.currentThread().getName() + " print : " + i);
        }
        // 该任务的返回值
        return "CallableDemo result from " + Thread.currentThread().getName();
    }
}