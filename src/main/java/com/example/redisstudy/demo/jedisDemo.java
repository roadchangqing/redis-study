/*
package com.example.redisstudy.demo;

import redis.clients.jedis.Jedis;

import java.util.*;

*/
/**
 * @author wuji
 * @version 1.0
 * @description TODO
 * @date 2023/6/20 14:47
 *//*

public class jedisDemo {
    public static void main(String[] args) {

        Jedis jedis = new Jedis("localhost", 6379);

        System.out.println(jedis.ping());

        jedis.set("k123","123");
        System.out.println(jedis.get("k123"));

        jedis.lpush("list1","k1","k2","k3");
        List<String> list1 = jedis.lrange("list1", 0, -1);
        for (String s : list1) {
            System.out.println(s);
        }

        jedis.hset("hkey1","h1","h2");
        System.out.println(jedis.hget("hkey1", "h1"));

        Map<String,String> map = new HashMap<String,String>();
        map.put("telphone","138xxxxxxxx");
        map.put("address","atguigu");
        map.put("email","zzyybs@126.com");

        jedis.hmset("hkey2",map);

        List<String> hmget = jedis.hmget("hkey2", "telphone", "address");
        for (String s : hmget) {
            System.out.println(s);
        }

        System.out.println("------------------");

        //set
        jedis.sadd("orders","jd001");
        jedis.sadd("orders","jd002");
        jedis.sadd("orders","jd003");
        Set<String> set1 = jedis.smembers("orders");
        for (String s : set1) {
            System.out.println(s);
        }

        jedis.srem("orders","jd002");
        System.out.println(jedis.smembers("orders").size());

        System.out.println("----------------");

        jedis.zadd("zset01",60d,"v1");
        jedis.zadd("zset01",70d,"v2");
        jedis.zadd("zset01",80d,"v3");
        jedis.zadd("zset01",90d,"v4");

        List<String> zset01 = jedis.zrange("zset01", 0, -1);
        zset01.forEach(System.out::println);
    }
}
*/
