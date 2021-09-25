package com.lxc.common.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/8/6
 * 点赞、观看、收藏数
 */
@Data
public class Nums {

    //浏览
    private Integer viewNum = 0;

    //点赞
    private Integer likeNum = 0;

    //收藏
    private Integer starNum = 0;

    //投币
    private Integer coinNum = 0;

    //评论
    private Integer commentNum = 0;

    //下载
    private Integer downloadNum = 0;

    //弹幕
    private Integer danmuNum = 0;

    //状态，默认为false
    private Boolean likeStatus = false;

    private Boolean starStatus = false;

    private Boolean coinStatus = false;

    public static Nums Map2Nums(Map map){
        Nums nums = new Nums();
        if (map.get("viewNum")!=null)
            nums.setViewNum((Integer)map.get("viewNum"));
        if (map.get("likeNum")!=null)
            nums.setLikeNum((Integer)map.get("likeNum"));
        if (map.get("starNum")!=null)
            nums.setStarNum((Integer)map.get("starNum"));
        if (map.get("coinNum")!=null)
            nums.setCoinNum((Integer)map.get("coinNum"));
        if (map.get("commentNum")!=null)
            nums.setCommentNum((Integer)map.get("commentNum"));
        if (map.get("downloadNum")!=null)
            nums.setDownloadNum((Integer)map.get("downloadNum"));
        if (map.get("danmuNum")!=null)
            nums.setDanmuNum((Integer)map.get("danmuNum"));
        return nums;
    }

}
