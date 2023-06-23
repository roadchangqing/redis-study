package com.example.redisstudy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wuji
 * @version 1.0
 * @description TODO
 * @date 2023/6/20 16:03
 */
@Service
@Slf4j
public class orderService {

    public static final String ORDER_KEY = "order:";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    public void add(){

        int keyId = ThreadLocalRandom.current().nextInt(1000)+1;
        String orderNo = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(ORDER_KEY+keyId,"京东订单"+ orderNo);
//        stringRedisTemplate.opsForValue().set(ORDER_KEY+keyId,"京东订单"+ orderNo);
        log.info("=====>编号"+keyId+"的订单流水生成:{}",orderNo);

    }

    public String get(Integer id){
       return (String) redisTemplate.opsForValue().get(ORDER_KEY+id);
//       return stringRedisTemplate.opsForValue().get(ORDER_KEY+id);
    }
}
