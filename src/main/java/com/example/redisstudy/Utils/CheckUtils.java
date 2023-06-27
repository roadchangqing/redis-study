package com.example.redisstudy.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wuji
 * @version 1.0
 * @description TODO
 * @date 2023/6/27 19:29
 */


@Component
@Slf4j
public class CheckUtils
{
    @Resource
    private RedisTemplate redisTemplate;

    //checkItem：白名单key名,        key：符合白名单条件的key（进行条件过滤的key）
    //流程就是，当布隆过滤器在的时候会去黑白名称寻找key，如果redis中存储的key在白名单，则可以查询redis，如果redis查不到就去查数据库
    public boolean checkWithBloomFilter(String checkItem,String key)
    {
        //获取key的hashCode
        int hashValue  = Math.abs(key.hashCode());
        //获取坑位
        long index = (long) (hashValue % Math.pow(2, 32));

        //getbit whitelistCustomer(白/黑过滤器key的名字) 坑位值
        Boolean existOK = redisTemplate.opsForValue().getBit(checkItem, index);

        //例如：key:customer:22  （存储在redis里面的key）  	它对应坑位index:1772098724	是否存在:true
        log.info("----->key:"+key+"\t对应坑位index:"+index+"\t是否存在:"+existOK);

        return existOK;
    }
}
