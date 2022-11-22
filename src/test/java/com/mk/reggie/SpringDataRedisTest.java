package com.mk.reggie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
//      redisTemplate.opsForValue().set("city","beijing");
//       String city = (String) redisTemplate.opsForValue().get("city");
//       System.out.println(city);

//       redisTemplate.opsForValue().set("key1","key1Value",10L, TimeUnit.SECONDS);
//       redisTemplate.opsForValue().get("key1");

       //如果key不存在就添加
//       Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city", "beijing");
//       System.out.println(aBoolean);
   }
   @Test
    public void testHash(){
       HashOperations operations = redisTemplate.opsForHash();
       //存值
       operations.put("001","name","小明");
       operations.put("001","age","23");
       operations.put("001","address","北京");
       //取值
       operations.get("001","address");
       //获取hash结构中所有的字段
       Set keys = operations.keys("001");
        for(Object key:keys){
            System.out.println(key);
        }
        //获取hash所有的值
       List values = operations.values("001");
        for(Object value:values){
            System.out.println(value);
        }
   }

   //list类型数据---按照插入顺序排序，可以有重复元素
    @Test
   public void testList(){
       ListOperations listOperations = redisTemplate.opsForList();
       //从左边存入
       listOperations.leftPush("myList","a");
       //存入多个
        listOperations.leftPushAll("myList","b","c","d");
        //取值
        List<String> myList = listOperations.range("myList", 0, -1);
        for(String list:myList){
            System.out.println(list);
        }
        //获取集合长度
        Long size = listOperations.size("myList");
        int lSize = size.intValue();
        //从右侧输出所有元素
        for(int i=0; i<=lSize;i++){
            String element = String.valueOf(listOperations.rightPop("myList"));
            System.out.println(element);
        }
    }

    /**
     * 操作set类型数据------元素不可以重复,无序
     */
    public void testSet(){
        SetOperations setOperations = redisTemplate.opsForSet();
        //存值
        setOperations.add("myset","a","b","c","a");
        //取值
        Set<String> mySet = setOperations.members("myset");
        for(String set:mySet){
            System.out.println(set);
        }
        //删除
        setOperations.remove("myset","a","b");
    }

    /**
     * Zset
     * 有序集合,不能有重复元素
     */
    @Test
    public void testZset(){
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        //存值
        zSetOperations.add("myZset","a",10.0);
        zSetOperations.add("myZset","b",33.0);
        zSetOperations.add("myZset","c",77.0);
        zSetOperations.add("myZset","a",30.0);
        //取值
        Set<String> myZset = zSetOperations.range("myZset", 0, -1);
        for(String zSet:myZset){
            System.out.println(zSet);
        }
        //修改分值
        zSetOperations.incrementScore("myZset","b",89.0);
        //删除
        zSetOperations.remove("myZset","a","b");
    }

    /**
     * 通用操作
     */
    public void testCommon(){
        //获取redis中所有的key
        Set<String> keys = redisTemplate.keys("*");
        System.out.println(keys);
        //判断某个key是否存在
        Boolean itcast = redisTemplate.hasKey("itcast");
        System.out.println(itcast);
        //删除指定的key
        redisTemplate.delete("itcast");
        //获取指定key对应的value的数据类型
        DataType dataType = redisTemplate.type("itcast");
        System.out.println(dataType);
    }

}
