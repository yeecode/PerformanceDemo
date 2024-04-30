package com.github.yeecode.hystrixdemo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Service
public class CollapserService {

    @HystrixCollapser(
            batchMethod = "queryUsersByIds",
            collapserProperties = {
                    // 批处理中允许的最大请求数为5
                    @HystrixProperty(name = "maxRequestsInBatch", value = "5"),
                    // 批处理的最大等待时间为500ms
                    @HystrixProperty(name = "timerDelayInMilliseconds", value = "500"),
            }
    )
    Future<String> queryUserNameById(Integer i) {
//        System.out.println("查询单个用户名，编号为: " + i );
//        QueryUserFromSomeWhere queryUserFromSomeWhere = new QueryUserFromSomeWhere(i);
//        FutureTask<String> futureTask = new FutureTask<>(queryUserFromSomeWhere);
//        new Thread(futureTask).start();
//        return futureTask;
        return null;
    }


    @HystrixCommand
    public List<String> queryUsersByIds(List<Integer> idList) {
        System.out.print("批量查询用户名，编号列表为: ");
        idList.forEach(x -> System.out.print(x.toString() + ";"));
        System.out.println();

        List<String> stringList = new ArrayList<>();
        idList.forEach(x -> stringList.add("易哥" + x));
        return stringList;
    }

//    static class QueryUserFromSomeWhere implements Callable<String> {
//        private Integer i;
//
//        QueryUserFromSomeWhere(Integer i) {
//            this.i = i;
//        }
//
//        @Override
//        public String call(){
//            return "易哥" +i;
//        }
//    }
}