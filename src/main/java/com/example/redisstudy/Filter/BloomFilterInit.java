package com.example.redisstudy.Filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author wuji
 * @version 1.0
 * @description TODO
 * @date 2023/6/27 19:25
 */
@Component
@Slf4j
public class BloomFilterInit {

    @Resource
    private RedisTemplate redisTemplate;

    //@PostConstruct修饰的方法会在服务器加载Servlet的时候运行
    @PostConstruct//初始化白名单数据，故意差异化数据演示效果......
    public void init(){

        //布隆过滤器：先去黑白名称寻找key，如果redis中存储的key在白名单，则可以查询redis，如果redis查不到就去查数据库
        //黑/白名单会写一个方法进行进行定义，符合条件才可以进一步访问，不符合条件会
        //白名单客户预加载到布隆过滤器
        String uid = "customer:22"; //自定义的key（只有是这个key才能通过布隆过滤器）

        //获取hashcode，取模（获取坑位）
        //1 计算hashcode，由于可能有负数，直接取绝对值 Math.abs:取绝对值
        int hashValue = Math.abs(uid.hashCode());

        //2 通过hashValue和2的32次方取余后，获得对应的下标坑位
        long index = (long) (hashValue % Math.pow(2, 32));

        log.info(uid+" 对应------坑位index:{}",index);

        //3 设置redis里面bitmap对应坑位，该有值设置为1         key index 1/0(true/false)
        redisTemplate.opsForValue().setBit("whitelistCustomer",index,true);

    }


}
