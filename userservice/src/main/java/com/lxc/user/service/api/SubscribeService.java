package com.lxc.user.service.api;

import com.lxc.common.entity.ResultBean;

/**
 * @author liuxianchun
 * @date 2021/8/1
 */
public interface SubscribeService {

    /**
     * 关注，uid关注人，flowerId被关注人
     * @param uid
     * @param flowerId
     * @param status
     * @return
     */
    ResultBean subscribe(Integer uid,Integer flowerId,Boolean status);

    ResultBean getSubscribeList(Integer uid);

    /**
     * 获取关注列表消息信息
     * @param token
     * @return
     */
    ResultBean getSubscribeDetail(String token);

    //点赞
    ResultBean like(Integer uid, String objectNo, String objectType,Boolean status);

    //收藏
    ResultBean star(Integer uid, String objectNo, String objectType,Boolean status);

    //投币
    ResultBean coin(Integer uid, String objectNo,Integer autherId);

    //获取点赞、投币、收藏数据
    ResultBean getNum(String type,String objectNo,Integer uid);

    //增加浏览量
    ResultBean addView(String objectNo, String objectType, String ip);

    //尝试下载文件
    ResultBean canDownloadFile(String token,String objectNo);

}
