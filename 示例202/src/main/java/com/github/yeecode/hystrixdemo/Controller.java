package com.github.yeecode.hystrixdemo;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@RestController
public class Controller {
    @Resource
    private IsolationService isolationService;

    @Resource
    private FallBackService fallBackService;

    @Resource
    private CircuitBreakerService circuitBreakerService;

    @Resource
    private LimiterService limiterService;

    @Resource
    private IsolationStrategyService isolationStrategyService;

    @Resource
    private CollapserService collapserService;

    @Resource
    private CacheService cacheService;

    /**
     * 演示Hystrix的降级功能
     * 该方法会以100ms间隔调用目标方法10次
     *
     * @return 10次目标方法返回值的拼接结果
     * @throws Exception 运行中发生的异常
     */
    @RequestMapping("/fallBack")
    public String fallBack() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            Thread.sleep(100);
            stringBuilder.append(fallBackService.queryUserNameById(i)).append("\r\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 演示Hystrix的熔断器功能
     * 该方法会以100ms间隔调用目标方法10次
     *
     * @return 10次目标方法返回值的拼接结果
     * @throws Exception 运行中发生的异常
     */
    @RequestMapping("/circuitBreaker")
    public String circuitBreaker() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            Thread.sleep(100);
            stringBuilder.append(circuitBreakerService.queryUserNameById(i)).append("\r\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 演示Hystrix的隔离功能
     * 该方法会同时调用三个不同的目标方法
     *
     * @return 3个目标方法返回值的拼接结果
     * @throws Exception 运行中发生的异常
     */
    @RequestMapping("/isolation")
    public String isolation() throws Exception {
        return "姓名：" + isolationService.queryUserNameById(1) + "\r\n" +
                "年龄：" + isolationService.queryUserAgeById(1) + "\r\n" +
                "位置：" + isolationService.queryUserLocationById(1) + "\r\n";
    }

    /**
     * 演示Hystrix的两种隔离机制的不同
     * 该方法会先以100ms间隔调用使用线程隔离的目标方法10次
     * 然后以100ms间隔调用使用信号量隔离的目标方法10次
     * 并且记录上述每次调用的响应耗时
     *
     * @return 20次目标方法返回值及响应耗时的拼接结果
     * @throws Exception 运行中发生的异常
     */
    @RequestMapping("/isolationStrategy")
    public String isolationStrategy() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            Thread.sleep(100);
            long beginTime = System.currentTimeMillis();
            stringBuilder.append(isolationStrategyService.queryUserNameByIdWithThread(i))
                    .append(" (+" + (System.currentTimeMillis() - beginTime) + "ms)")
                    .append("\r\n");
        }
        for (int i = 1; i <= 5; i++) {
            Thread.sleep(100);
            long beginTime = System.currentTimeMillis();
            stringBuilder.append(isolationStrategyService.queryUserNameByIdWithSemaphore(i))
                    .append(" (+" + (System.currentTimeMillis() - beginTime) + "ms)")
                    .append("\r\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 演示Hystrix的限流功能
     * 该方法会使用多线程方式调用目标方法100次。每次间隔时间100ms
     *
     * @return 100次调用的目标方法返回值的拼接结果
     * @throws Exception 运行中发生的异常
     */
    @RequestMapping("/limiter")
    public String limiter() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        // 创建100个调用limitService.sayHi方法的任务
        List<Callable<String>> callableList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            int finalI = i;
            callableList.add(() -> limiterService.queryUserNameById(finalI));
        }

        // 创建100个FutureTask以接收全部任务的返回值
        List<FutureTask<String>> futureTaskList = new ArrayList<>();
        for (Callable<String> callable : callableList) {
            futureTaskList.add(new FutureTask<>(callable));
        }

        // 以100ms为间隔，依次触发任务
        for (FutureTask<String> futureTask : futureTaskList) {
            Thread thread = new Thread(futureTask);
            thread.start();
            Thread.sleep(100);
        }

        // 接收任务的返回值
        for (FutureTask<String> futureTask : futureTaskList) {
            stringBuilder.append(futureTask.get()).append("\r\n");
        }

        return stringBuilder.toString();
    }

    /**
     * 演示Hystrix的请求缓存功能
     * 该方法先直接调用目标方法5次
     * 然后以先清理缓存再调用的方式，执行5次
     *
     * @return 10次目标方法返回值的拼接结果
     * @throws Exception 运行中发生的异常
     */
    @RequestMapping("/cache")
    public String cache() {
        StringBuilder stringBuilder = new StringBuilder();
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            for (int i = 1; i <= 3; i++) {
                stringBuilder.append(cacheService.queryUserNameById(303)).append("\r\n");
                stringBuilder.append(cacheService.queryUserNameById(316)).append("\r\n");
                stringBuilder.append(cacheService.queryUserNameById(808)).append("\r\n");
                cacheService.clearUserResult(808);
            }
        } finally {
            context.close();
        }
        return stringBuilder.toString();
    }


    /**
     * 演示Hystrix的方法合并功能
     * 该方法先以变化的时间间隔调用目标方法30次，后统一收集这30次的调用结果
     * 然后在调用目标方法10次，并每次调用后都收集调用结果
     *
     * @return 40次目标方法返回值的拼接结果
     * @throws Exception 运行中发生的异常
     */
    @RequestMapping("/collapser")
    public String collapser() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            List<Future<String>> futureList = new ArrayList<>();

            // 连续的10次调用
            for (int i = 1; i <= 10; i++) {
                futureList.add(collapserService.queryUserNameById(i));
            }
            Thread.sleep(2000);

            // 间隔1000ms的10次调用
            for (int i = 11; i <= 20; i++) {
                Thread.sleep(1000);
                futureList.add(collapserService.queryUserNameById(i));
            }
            Thread.sleep(2000);

            // 间隔150ms的10次调用
            for (int i = 21; i <= 30; i++) {
                Thread.sleep(150);
                futureList.add(collapserService.queryUserNameById(i));
            }
            Thread.sleep(2000);

            // 获取以上30次调用的结果
            for (Future<String> future : futureList) {
                stringBuilder.append(future.get()).append("\r\n");
            }

            // 连续进行10次调用，但每次调用后立刻同步获取结果
            for (int i = 31; i <= 40; i++) {
                stringBuilder.append(collapserService.queryUserNameById(i).get()).append("\r\n");
            }
        } finally {
            context.close();
        }
        return stringBuilder.toString();
    }
}
