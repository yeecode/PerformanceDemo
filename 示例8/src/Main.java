import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class Main {
    public static void main(String[] args) {
        try {
            // 创建总分线程池
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            // 计算某个数字的阶乘
            Integer num = 7;
            Future<Integer> result = forkJoinPool.submit(new FactorialCalculator(num));
            System.out.println(num +"! = "+result.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class FactorialCalculator extends RecursiveTask<Integer> {
        private static final long serialVersionUID = 1L;
        private static final int THRESHOLD = 2;
        private int start;
        private int end;

        public FactorialCalculator(int end) {
            this.start = 1;
            this.end = end;
        }

        public FactorialCalculator(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Integer compute() {
            String threadName = Thread.currentThread().getName();
            int result = 1;
            if ((end - start) < THRESHOLD) {
                for (int i = start; i <= end; i++) {
                    result *= i;
                }
                System.out.println(threadName + "计算完成[" + start +"," + end+"]之间的乘积，得到结果为："+result);
            } else {
                int middle = (start + end) >>> 1;
                // 进行任务的拆分与分别计算
                System.out.println(threadName + "将[" + start +"," + end+"]任务拆分为[" + start +"," + middle+"] 和 [" +  (middle+1) +"," + end+"] 两个子任务");
                FactorialCalculator leftPartCalculator = new FactorialCalculator(start, middle);
                FactorialCalculator rightPartCalculator = new FactorialCalculator(middle + 1, end);
                leftPartCalculator.fork();
                rightPartCalculator.fork();
                // 合并任务结果
                result = leftPartCalculator.join() * rightPartCalculator.join();
                System.out.println(threadName + "将[" + start +"," + middle+"] 和 [" +  (middle+1) +"," + end+"] 两个子任务的结果进行了合并，得到[" + start +"," + end+"]之间的乘积，得到结果为："+result);
            }
            return result;
        }
    }
}

