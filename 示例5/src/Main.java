public class Main {
    static ThreadLocal<Integer> selfNumber = new ThreadLocal<>();
    static ThreadLocal<String> selfString = new ThreadLocal<>();

    public static void main(String[] args) {
        try {
            String threadName = Thread.currentThread().getName();
            Thread thread01 = new Thread(new Task01());
            Thread thread02 = new Thread(new Task02());
            thread01.start();
            thread02.start();
            Thread.sleep(2L);
            System.out.println(threadName + "  selfNumber :" + selfNumber.get());
            System.out.println(threadName + "  selfString :" + selfString.get());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class Task01 implements Runnable {
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + "  selfNumber :" + selfNumber.get());
            System.out.println(threadName + "  selfString :" + selfString.get());
            selfNumber.set(3001);
            selfString.set("hello");
            System.out.println(threadName + "  selfNumber :" + selfNumber.get());
            System.out.println(threadName + "  selfString :" + selfString.get());
        }
    }

    private static class Task02 implements Runnable {
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + "  selfNumber :" + selfNumber.get());
            System.out.println(threadName + "  selfString :" + selfString.get());
            selfNumber.set(8002);
            selfString.set("world");
            System.out.println(threadName + "  selfNumber :" + selfNumber.get());
            System.out.println(threadName + "  selfString :" + selfString.get());
        }
    }
}

