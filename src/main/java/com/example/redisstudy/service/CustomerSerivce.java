package com.example.redisstudy.service;


import com.example.redisstudy.Utils.CheckUtils;
import com.example.redisstudy.entities.Customer;
import com.example.redisstudy.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wuji
 * @version 1.0
 * @description TODO
 * @date 2023/6/27 16:08
 */
@Service
@Slf4j
public class CustomerSerivce {

    public static final String CACHE_KEY_CUSTOMER = "customer:";

    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private RedisTemplate redisTemplate;


    //添加数据并存储道redis中
    public void add(Customer customer){
        int i = customerMapper.insert(customer);

        if (i>0){
            //到数据库里面，重新捞出新数据出来，做缓存
            customer = customerMapper.selectByPrimaryKey(customer.getId());

            //缓存key
            String key=CACHE_KEY_CUSTOMER+customer.getId();
            //往mysql里面插入成功随后再从mysql查询出来，再插入redis
            redisTemplate.opsForValue().set(key,customer);
        }
    }


    //查询数据
    public Customer select(Integer customerId){
        Customer customer = null;

        //key名称
        String key=CACHE_KEY_CUSTOMER+customerId;

         customer = (Customer) redisTemplate.opsForValue().get(key);

        if (customer==null){
            customer = customerMapper.selectByPrimaryKey(customerId);

            if (customer!=null){
                redisTemplate.opsForValue().set(key,customer);
            }
        }

        //不等于null就直接返回从redis里面查到的数据
        return customer;

    }


    //修改数据库的同时，清除旧的redis缓存，然后添加新的redis缓存
    public Customer update(Customer customer){

        Integer customerId = customer.getId();

        //key名称
        String key=CACHE_KEY_CUSTOMER+customerId;

        int i = customerMapper.updateByPrimaryKey(customer);
        if (i>0){
            //先清除旧的缓存，再添加新缓存(也可以获取旧值设置新值（getAndSet）)
             redisTemplate.delete(key);
            redisTemplate.opsForValue().set(key,customer);
        }

        //不等于null就直接返回从redis里面查到的数据
        return null;

    }


    //删除数据,删除数据库的同时清理redis缓存
    public Customer delete(Integer customerId){

        //key名称
        String key=CACHE_KEY_CUSTOMER+customerId;

        System.out.println(key);

        int i = customerMapper.deleteByPrimaryKey(customerId);

        if (i>0){
            //清除redis缓存
            redisTemplate.delete(key);
        }

        //不等于null就直接返回从redis里面查到的数据
        return null;

    }
    /**
     * BloomFilter → redis → mysql
     * 白名单：whitelistCustomer
     * @param customerId
     * @return
     */

    @Resource
    private CheckUtils checkUtils;

    public Customer findCustomerByIdWithBloomFilter (Integer customerId)
    {
        Customer customer = null;

        //缓存key的名称
        String key = CACHE_KEY_CUSTOMER + customerId;

        //布隆过滤器check，无是绝对无，有是可能有
        //===============================================
        //布隆过滤器：先去黑白名称寻找key，如果redis中存储的key在白名单，则可以查询redis，如果redis查不到就去查数据库
        //黑/白名单会写一个方法进行进行定义（这里是在filter包下，我把方法的代码复制在这下面），符合条件才可以进一步访问，不符合条件会直接进行拦截
        if (!checkUtils.checkWithBloomFilter("whitelistCustomer",key)){
            log.info("白名单无此顾客信息:{}",key);
            return null;
        }
        //===============================================

        //1 查询redis
        customer = (Customer) redisTemplate.opsForValue().get(key);
        //redis无，进一步查询mysql
        if (customer == null) {
            //2 从mysql查出来customer
            customer = customerMapper.selectByPrimaryKey(customerId);
            // mysql有，redis无
            if (customer != null) {
                //3 把mysql捞到的数据写入redis，方便下次查询能redis命中。
                redisTemplate.opsForValue().set(key, customer);
            }
        }
        return customer;
    }

    /*
    也就是
    //@PostConstruct修饰的方法会在服务器加载Servlet的时候运行
    @PostConstruct//初始化白名单数据，故意差异化数据演示效果......
    public void init(){

        //白名单客户预加载到布隆过滤器
        String uid = "customer:22"; //redis里面的key

        //获取hashcode，取模（获取坑位）
        //1 计算hashcode，由于可能有负数，直接取绝对值 Math.abs:取绝对值
        int hashValue = Math.abs(uid.hashCode());

        //2 通过hashValue和2的32次方取余后，获得对应的下标坑位
        long index = (long) (hashValue % Math.pow(2, 32));

        log.info(uid+" 对应------坑位index:{}",index);

        //3 设置redis里面bitmap对应坑位，该有值设置为1         key index 1/0(true/false)
        redisTemplate.opsForValue().setBit("whitelistCustomer",index,true);

    }*/

}
