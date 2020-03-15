import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("main function is in Thread :" + Thread.currentThread().getName());
        NewThread newThread = new NewThread();
        // 启动新线程
        newThread.start();
        for (int i = 0; i < 3; i++) {
            System.out.println("Thread " + Thread.currentThread().getName() + " print : " + i);
        }
    }
}
