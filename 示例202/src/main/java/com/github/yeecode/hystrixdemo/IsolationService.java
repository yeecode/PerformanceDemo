package com.github.yeecode.hystrixdemo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.stereotype.Service;

@Service
public class IsolationService {
    @HystrixCommand(
            commandKey = "IsolationService_queryUserNameById",
            fallbackMethod = "fallBackForName",
            commandProperties = {
                    // 超时判定时间设置为500ms
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"),
            }
    )
    String queryUserNameById(int i) {
        System.out.println("正在查询用户名，用户编号为" + i);
        return "易哥";
    }

    @HystrixCommand(fallbackMethod = "fallBackForAge",
            commandProperties = {
                    // 超时判定时间设置为500ms
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"),
            }
    )
    String queryUserAgeById(int i) {
        System.out.println("正在查询用户年龄，用户编号为" + i);
        return "18";
    }

    @HystrixCommand(fallbackMethod = "fallBackForLocation",
            commandProperties = {
                    // 超时判定时间设置为500ms
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"),
            }
    )
    String queryUserLocationById(int i) throws InterruptedException {
        // 故意延时，模拟堵塞
        Thread.sleep(1000);
        System.out.println("正在查询用户位置，用户编号为" + i);
        return "西子湖畔";
    }

    String fallBackForName(int i) {
        System.out.print("生成默认用户名信息，用户编号为" + i);
        return "平台用户";
    }

    String fallBackForAge(int i) {
        System.out.print("生成默认年龄信息，用户编号为" + i);
        return "年富力强";
    }

    String fallBackForLocation(int i) {
        System.out.print("生成默认位置信息，用户编号为" + i);
        return "太阳系";
    }
}
