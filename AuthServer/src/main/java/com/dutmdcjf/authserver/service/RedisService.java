package com.dutmdcjf.authserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;

    ValueOperations<String, Object> values = redisTemplate.opsForValue();

    public void setValues(String redisId, Object token, Long expire){
        values.set(redisId, token, expire, TimeUnit.MILLISECONDS);
    }

    public Object getValues(String redisId){
        return values.get(redisId);
    }

    public void delValues(String redisId){
        redisTemplate.delete(redisId);
    }
}
