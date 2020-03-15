import java.util.concurrent.*;

public class Main {
    // 假设初始时存在0个许可
    private static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) {
        try {
            // 生产许可和释放许可数目不同的六个线程
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new RunDemo(1, 2));
            executorService.execute(new RunDemo(0, 4));
            executorService.execute(new RunDemo(3, 4));
            executorService.execute(new RunDemo(2, 0));
            executorService.execute(new RunDemo(0, 1));
            executorService.execute(new RunDemo(2, 1));
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class RunDemo implements Runnable {
        private Integer inputCount;
        private Integer outputCount;

        public RunDemo(Integer inputCount, Integer outputCount) {
            this.inputCount = inputCount;
            this.outputCount = outputCount;
        }

        @Override
        public void run() {
            try {
                String threadName = Thread.currentThread().getName();
                // 必须获取到足够的许可，线程才能开展工作
                semaphore.acquire(inputCount);
                System.out.println(threadName + "获取" + inputCount + "个许可开始工作……");
                Thread.sleep((long) (Math.random() * 10));
                // 必须通过semaphore.availablePermits()+outputCount计算该线程释放后的总闲置许可数目，因为释放的瞬间可能会被其他线程acquire走
                System.out.println(threadName + "完成工作释放" + outputCount + "个许可,当前共有" + (semaphore.availablePermits() + outputCount) + "个可给出的许可。");
                // 工作结束后释放一些许可
                semaphore.release(outputCount);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

