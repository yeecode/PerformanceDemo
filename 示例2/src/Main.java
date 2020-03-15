public class Main {
    public static void main(String[] args) {
        System.out.println("main function is in Thread :" + Thread.currentThread().getName());
        // 创建一个线程并将任务载入
        Thread thread = new Thread(new Task());
        // 启动新线程
        thread.start();
        for (int i = 0; i < 3; i++) {
            System.out.println("Thread " + Thread.currentThread().getName() + " print : " + i);
        }
    }
}