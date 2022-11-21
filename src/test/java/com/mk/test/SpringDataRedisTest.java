package com.mk.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

/*RedisTemplate
* ValueOperations  k-v
* SetOperations  set类型数据
* ZsetOperations  zset类型
* HashOperations  map类型
* ListOperations  list类型
* */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

   @Test
   public void testString(){
       ValueOperations valueOperations=redisTemplate.opsForValue();
   }

}
