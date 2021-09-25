package com.lxc.system.feign.fallback;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.system.feign.api.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author liuxianchun
 * @date 2021/5/23
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public ResultBean findAccount(String account) {
        return null;
    }

    @Override
    public ResultBean findUserName(String username) {
        return null;
    }

    @Override
    public ResultBean isLogin(String token) {
        return null;
    }

    @Override
    public ResultBean userLogout(String token) {
        return null;
    }

    @Override
    public ResultBean userLogin(String loginCode, User user) {
        return null;
    }

    @Override
    public ResultBean userRegister(User user) {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getVerifyCode() {
        return null;
    }

    @Override
    public ResultBean getMailCode(String account){
        return null;
    };

}
