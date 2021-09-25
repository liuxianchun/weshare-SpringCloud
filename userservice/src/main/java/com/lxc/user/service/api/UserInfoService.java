package com.lxc.user.service.api;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/7/7
 */
public interface UserInfoService {

    ResultBean isRoleCheck(String token);

    User getUserByToken(String token);

    User getUserByUid(Integer uid);

    ResultBean getUserInfoByIds(List<Integer> ids);

    ResultBean getUserInfoById(Integer uid);

    //刷新redis
    void refreshUser(Integer uid);

    //更新用户头像
    ResultBean updateAvatar(Integer uid,String path);

    //更新用户信息
    ResultBean updateUser(String token,User user);

    //增加用户硬币、成长值
    ResultBean addCoinExp(Integer uid,Integer coin,Integer scoin,Integer growth);

}
