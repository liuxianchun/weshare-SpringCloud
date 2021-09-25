package com.lxc.system.feign.fallback;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.system.feign.api.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/7/7
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public ResultBean isRoleCheck(String token) {
        return null;
    }

    @Override
    public User getUserByToken(String token) {
        return null;
    }

    @Override
    public User getUserByUid(Integer uid) {
        return null;
    }

    @Override
    public ResultBean getUserInfoByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public ResultBean getUserInfoById(Integer uid) {
        return null;
    }

    @Override
    public ResultBean getSubscribeList(String token) {
        return null;
    }

    @Override
    public ResultBean getSubscribeDetail(String token) {
        return null;
    }

    @Override
    public ResultBean subscribe(String token, Integer flowerId, Boolean status) {
        return null;
    }

    @Override
    public ResultBean like(String token, String objectNo, String objectType, Boolean status) {
        return null;
    }

    @Override
    public ResultBean star(String token, String objectNo, String objectType, Boolean status) {
        return null;
    }

    @Override
    public ResultBean coin(String token, String objectNo,Integer autherId) {
        return null;
    }

    @Override
    public ResultBean getNum(String token, String objectNo, String objectType) {
        return null;
    }

    @Override
    public ResultBean addView(String objectNo, String objectType, String ip) {
        return null;
    }

    @Override
    public ResultBean canDownloadFile(String token, String objectNo) {
        return null;
    }

    @Override
    public ResultBean updateAvatar(Integer uid, String path) {
        return null;
    }

    @Override
    public ResultBean updateUser(String token, User user) {
        return null;
    }
}
