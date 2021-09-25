package com.lxc.file;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.nacos.common.util.Md5Utils;
import com.lxc.file.pojo.Video;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: liuxianchun
 * @Date: 2021/08/20
 * @Description:
 */
@SpringBootTest
public class threadtest {

    private Map<String,String> map = new ConcurrentHashMap<>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private ExecutorService testExecutor = Executors.newCachedThreadPool();

    private String request(String key){
        System.out.println("接收到请求,key="+key);
        String result = "ready";
        long time1 = System.currentTimeMillis();
        if (!map.containsKey(key)){
            map.put(key,"ready");
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                        map.put(key,"true");
                        System.out.println("请求成功，将结果放入map");
                    } catch (InterruptedException e) {
                        map.put(key,"false");
                        e.printStackTrace();
                    }
                }
            });
        }
        int count = 0;
        while(count<30){
            if (!"ready".equals(map.get(key))){
                result = map.get(key);
                break;
            }
            try {
                System.out.println("等待请求结果...");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
        }
        long time2 = System.currentTimeMillis();
        System.out.println("请求"+key+"返回结果:"+result+"请求"+key+"用时"+(time2-time1)/1000.0+"s");
        return result;
    }

    @Test
    void init(){
        map = new ConcurrentHashMap<>();
        Video v1 = new Video();
        Video v2 = new Video();
        v1.setTitle("asd");
        v1.setSnowFlakeId(998L);
        v2.setTitle("asd");
        v2.setSnowFlakeId(998L);
        String s1 = DigestUtil.md5Hex(v1.toString());
        System.out.println(s1);
        String s2 = DigestUtil.md5Hex(v1.toString());
        System.out.println(s2);

    }

    @Test
    void test1(){
        testExecutor.submit(()->{
            request("key1");
        });

        request("key2");
        request("key1");
        request("key3");
        request("key2");
    }


}
