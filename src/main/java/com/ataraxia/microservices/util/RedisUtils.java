package com.ataraxia.microservices.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author MyPC
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisUtils {

    @Resource
    private RedisTemplate redisTemplate;

    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

}
