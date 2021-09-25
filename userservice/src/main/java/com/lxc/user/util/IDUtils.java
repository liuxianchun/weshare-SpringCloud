package com.lxc.user.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/07
 * @Description:
 */
@Component
public class IDUtils {

    private static final AtomicInteger userInteger = new AtomicInteger(1000);
    private static final AtomicInteger videoInteger = new AtomicInteger(1000);
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");
    private static final String[] arr = {
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
            "0","1","2","3","4","5","6","7","8","9"
    };
    private static final BigInteger len = new BigInteger(arr.length+"");

    @Value("${weshare.datacenterId}")
    private String dataid;
    @Value("${weshare.workerId}")
    private String machineid;

    public String createUserId(){
        if(userInteger.get()>9900)
            userInteger.getAndSet(1000);
        return System.currentTimeMillis()+dataid+machineid+userInteger.getAndIncrement();
    }

    public String createVideoId(){
        if(videoInteger.get()>9900)
            videoInteger.getAndSet(1000);
        String seq = videoInteger.getAndIncrement()+"";
        String millis = System.currentTimeMillis()+"";
        String sid = millis.substring(0,1)+seq.substring(3,4)+millis.substring(12,13)+dataid+
                seq.substring(2,3)+millis.substring(3,5)+millis.substring(11,12) +millis.substring(1,3)+
                seq.substring(0,2)+millis.substring(5,8)+machineid+millis.substring(8,11);
        BigInteger bigInteger = new BigInteger(sid);
        System.out.println(bigInteger);
        String res = "BV" + num2str(bigInteger, "");
        System.out.println("create new videoid:"+res);
        return res;
    }

    private String num2str(BigInteger integer, String str){
        if(integer.equals(BigInteger.ZERO)){
            return str;
        }else{
            BigInteger div = integer.divide(len);
            BigInteger mod = integer.mod(len);
            return num2str(div,arr[mod.intValue()]+str);
        }
    }

    public static void main(String[] args) {
        IDUtils idUtils = new IDUtils();
        for(int i=0;i<100;i++){
            System.out.println(idUtils.createVideoId());
        }
    }
}
