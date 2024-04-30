package com.github.yeecode.hystrixdemo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    @CacheResult(cacheKeyMethod = "getUserCacheKey")
    @HystrixCommand(
            commandKey = "CacheService_queryUserNameById",
            commandProperties = {
                    // 启用请求缓存功能（默认也是启用的）
                    @HystrixProperty(name = "requestCache.enabled", value = "true"),
            }
    )
    String queryUserNameById(int i) {
        System.out.println("正在查询用户名，用户编号为" + i);
        return "易哥" + i;
    }

    @CacheRemove(commandKey = "CacheService_queryUserNameById",
            cacheKeyMethod = "getUserCacheKey")
    @HystrixCommand
    void clearUserResult(int i) {
        System.out.println("入参" + i + "对应的缓存已清空 ");
    }

    String getUserCacheKey(int i) {
        return "CACHE_KEY_" + i;
    }

}
