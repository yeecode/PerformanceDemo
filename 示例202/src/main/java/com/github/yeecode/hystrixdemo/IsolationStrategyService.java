package com.github.yeecode.hystrixdemo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class IsolationStrategyService {
    @HystrixCommand(
            fallbackMethod = "fallBackForQueryUserNameById",
            commandProperties = {
                    // 采用线程隔离模式
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                    // 超时判定时间设置为50ms
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50"),
            }
    )
    String queryUserNameByIdWithThread(int i) throws InterruptedException {
        System.out.println("正在使用线程模式查询用户名，用户编号为" + i);
        Thread.sleep(5000);
        return "易哥" + i;
    }

    @HystrixCommand(
            fallbackMethod = "fallBackForQueryUserNameById",
            commandProperties = {
                    // 采用信号量隔离模式
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
                    // 超时判定时间设置为50ms
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50"),
            }
    )
    String queryUserNameByIdWithSemaphore(int i) throws InterruptedException {
        System.out.println("正在使用信号量模式查询用户名，用户编号为" + i);
        Thread.sleep(5000);
        return "易哥" + i;
    }

    String fallBackForQueryUserNameById(int i) {
        return "平台用户" + i;
    }
}