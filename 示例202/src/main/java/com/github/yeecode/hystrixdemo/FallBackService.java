package com.github.yeecode.hystrixdemo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FallBackService {
    @HystrixCommand(
            commandKey = "FallBackService_queryUserNameById",
            fallbackMethod = "queryDefaultUserName",
            commandProperties = {
                    // 超时判定时间设置为30ms
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30"),
            }
    )
    String queryUserNameById(int i) throws InterruptedException {
        System.out.print("正在查询用户名，用户编号为" + i);
        int randomValue = new Random().nextInt(100);
        if (randomValue < 30) {
            // 约30%概率会触发异常
            System.out.println(" : 查询发生异常 ");
            throw new RuntimeException();
        } else if (randomValue < 60) {
            // 约30%概率会触发超时
            System.out.println(" : 查询发生延迟 ");
            Thread.sleep(50);
            System.out.println("编号" + i +"的用户名查询终于完成");
            return "编号" + i +"的用户名查询超时";
        } else {
            // 约40%概率会正常执行
            System.out.println(" : 查询成功 ");
            return "易哥" + i;
        }
    }

    String queryDefaultUserName(int i) {
        System.out.println("生成默认用户名，用户编号为" + i);
        return "平台用户" + i;
    }
}