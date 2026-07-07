package com.inspire.blog_jpa.features.common.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

    private static final long RT_TTL = 60 * 60 * 24 * 7 ;  // 7일
    // "RT:email"
    private final RedisTemplate<String, Object> redisTemplate;
    public void saveToken(String email, String rt) {
        System.out.println(">>>> debug RedisService save rt token ");
        redisTemplate.opsForValue()
            .set("RT:"+email, rt, RT_TTL, TimeUnit.SECONDS);
    }
    public void deleteToken(String email) {
        System.out.println(">>>> debug RedisService delete rt token ");
        redisTemplate.delete("RT:"+email);
    }
    
    public String findByEmail(String email) {
        return null ;
    }

}
