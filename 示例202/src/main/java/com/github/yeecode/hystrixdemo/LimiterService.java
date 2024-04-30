package com.github.yeecode.hystrixdemo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.stereotype.Service;

@Service
public class LimiterService {
    @HystrixCommand(
            commandKey = "LimiterService_queryUserNameById",
            fallbackMethod = "fallBackForSayHi",
            threadPoolProperties = {
                    // 核心线程设置为5
                    @HystrixProperty(name = "coreSize", value = "5"),
                    // 最大排队队列长度为10
                    @HystrixProperty(name = "maxQueueSize", value = "10"),
                    // 队列中最多有2个任务排队，超过该数目的任务则拒绝
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "2"),
            },
            commandProperties = {
                    // 关掉熔断器，避免熔断器影响
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "false"),
                    // 超时判定时间设置为2000ms，即下方的1000ms延时不会引发超时
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
            }
    )
    String queryUserNameById(int i) throws InterruptedException {
        System.out.println("正在查询用户名，用户编号为" + i + "。该查询所在的线程名：" + Thread.currentThread().getName());
        // 故意增加处理时延，以模拟堵塞
        Thread.sleep(1000);
        return "易哥" + i;
    }

    String fallBackForSayHi(int i) {
        System.out.println("平台用户" + i);
        return "平台用户" + i;
    }
}
