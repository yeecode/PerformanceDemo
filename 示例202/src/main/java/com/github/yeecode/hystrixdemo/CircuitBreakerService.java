package com.github.yeecode.hystrixdemo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CircuitBreakerService {
    @HystrixCommand(
            commandKey = "CircuitBreakerService_queryUserNameById",
            fallbackMethod = "fallBackForSayHi",
            commandProperties = {
                    // 开启熔断器（可以省略，因为默认就是开启的）
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    // 超时判定时间设置为30ms
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30"),
                    // 时间窗内的最小采样值为5
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    // 错误超过30%时即引发熔断
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "30"),
                    // 熔断后恢复时间为500ms
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "500")
            })
    String queryUserNameById(int i) throws InterruptedException {
        System.out.print("正在查询用户名，用户编号为" + i);
        int randomValue = new Random().nextInt(100);
        if (randomValue <= 30) {
            // 约30%概率会触发异常
            System.out.println(" : 查询发生异常 ");
            throw new RuntimeException();
        } else if (randomValue <= 60) {
            // 约30%概率会触发超时
            System.out.println(" : 查询发生延迟 ");
            Thread.sleep(50);
            System.out.println("编号" + i +"的用户名查询终于完成");
            return "编号" + i +"的用户名查询超时";
        } else {
            // 约40%概率会正常执行
            System.out.println(" : 查询成功 ");
            return "易哥 " + i;
        }
    }

    public String fallBackForSayHi(int i) {
        System.out.println("生成默认用户名，用户编号为" + i);
        return "平台用户" + i;
    }
}