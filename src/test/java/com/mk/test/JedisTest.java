package com.mk.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * 使用Jedis操作Redis
 */
public class JedisTest {
    @Test
    public void testRedis(){
        //连接
        Jedis jedis=new Jedis("localhost",6379);
        //执行具体操作
        /**
         * string
         */
//        jedis.set("username","xiaoming");
//        String username = jedis.get("username");
//        System.out.println(username);
//        jedis.del("username");

        /**
         * hash
         */
//        jedis.hset("myhash","addr","beijing");
//        String hget = jedis.hget("myhash", "addr");
        jedis.hdel("myhash","addr");
//        System.out.println(hget);

        //关闭连接
        jedis.close();

    }
}
