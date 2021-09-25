package com.lxc.file;

import com.lxc.common.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liuxianchun
 * @date 2021/7/28
 */
@SpringBootTest
public class Redistest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RedisUtil redis;

    @Test
    void test1(){
        Long test_time_key = redisTemplate.opsForValue().increment("test_time_key");
        boolean test_time_key1 = redis.expire("test_time_key", 120);
        System.out.println(test_time_key1);
    }

    @Test
    void test2(){
        List<Integer> list = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 7);
        redis.sSet("set",13);
        redis.sSet("set",list2);
        redis.sSet("star-video",1234123L);
        redis.sSet("star-video",123522233L);
        redis.sSet("star-video",123833223L);
        redis.lSet("list",list,1200);
        redis.lSet("list",list2,1200);
        List<Object> set = redis.lGet("set", 0, 100);
        Set<Object> set1 = redis.sGet("set");
        Long size = redis.sGetSetSize("set");
        System.out.println("size:"+size);
        System.out.println(set1);
        redis.hincr("hash","1234",1);
        redis.hincr("hash","1236",1);
    }

    @Test
    void test3(){
        redis.hset("test-hash","1026594948",9);
        redis.hset("test-hash","105265916",10);
        redisTemplate.opsForSet().add("test-set-9",1265);
       // redisTemplate.opsForSet().add("test-set-9",169954);
       // redisTemplate.opsForSet().remove("test-set-9",1265);
        Long aLong = redis.sDel("test-set-9", 169954);
        System.out.println(aLong);
        //redisTemplate.opsForSet().move()
        String key = "video"+":num:"+"1226";
        redis.hset(key,"aNum",1);
        Integer coinNum = (Integer) redis.hget(key, "aNum");
        Map map = redis.hmget(key);
        System.out.println(coinNum);
        System.out.println(map);
        System.out.println(map.get("aNum"));
    }

    @Test
    void test4(){
        /*long time1 = System.currentTimeMillis();
        for (int i=0;i<1000000;i++){
            redis.set("big:"+i,i);
        }
        long time2 = System.currentTimeMillis();
        System.out.println("生成1000000个key用时:"+(time2-time1)/1000.0+"s");

        long time3 = System.currentTimeMillis();
        Set<String> keys = redis.keys("big:*");
        long time4 = System.currentTimeMillis();
        System.out.println(keys.size());
        System.out.println("遍历1000000个key用时:"+(time4-time3)/1000.0+"s");*/

        long time5 = System.currentTimeMillis();
        for (int i=0;i<1000000;i++){
            redis.del("big:"+i);
        }
        long time6 = System.currentTimeMillis();
        System.out.println("删除100000个key用时:"+(time6-time5)/1000.0+"s");
    }

}
