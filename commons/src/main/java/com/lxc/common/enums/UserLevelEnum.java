package com.lxc.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liuxianchun
 * @Date: 2021/07/26
 * @Description: 用户等级
 */
public enum UserLevelEnum {

    Lv1(1,0),
    Lv2(2,1000),
    Lv3(3,2800),
    Lv4(4,5400),
    Lv5(5,9500),
    Lv6(6,21000),
    Lv7(7,45000),
    Lv8(8,100000),
    MAX(99,Integer.MAX_VALUE);

    /*等级*/
    private int level;
    /*成长值*/
    private int growth;

    UserLevelEnum(int level,int growth){
        this.level = level;
        this.growth = growth;
    }

    public static Integer getLevel(int growth){
        int level = 0;
        for (UserLevelEnum levelEnum:values()){
            if (growth >= levelEnum.growth){
                level = levelEnum.level;
            }else
                break;
        }
        return level;
    }

    public static Map getUserLevelMap(int growth){
        Map<String, Object> map = new HashMap<>(4);
        map.put("growth",growth);
        UserLevelEnum nextLevel = Lv1;
        UserLevelEnum nowLevel = Lv1;
        for (UserLevelEnum levelEnum:values()){
            if (growth < levelEnum.growth){
                nextLevel = levelEnum;
                break;
            }
            nowLevel = levelEnum;
        }
        map.put("nextGrowth",nextLevel.equals(MAX)?null:nextLevel.growth);
        map.put("level",nowLevel.level);
        map.put("rate",nowLevel.equals(Lv8)?100:growth*100/nextLevel.growth);
        return map;
    }

}
