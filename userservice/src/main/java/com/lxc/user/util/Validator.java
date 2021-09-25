package com.lxc.user.util;

import com.lxc.common.entity.user.User;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/20
 * @Description:
 */
public class Validator {

    public static final String regex_account = "\\w+@\\w+\\.(com|net.cn)";
    public static final String regex_username = "[a-zA-Z0-9\\u4e00-\\u9fa5]{4,16}";

    public static boolean validateUser(User user){
        if(user==null)
            return false;
        if(StringUtils.isEmpty(user.getUsername()))
            return false;
        if(StringUtils.isEmpty(user.getPassword()))
            return false;
        if(StringUtils.isEmpty(user.getCheckPass()))
            return false;
        if(StringUtils.isEmpty(user.getVerifyCode()))
            return false;
        if(!user.getPassword().equals(user.getCheckPass()))
            return false;
        if(!Pattern.matches(regex_account,user.getAccount()))
            return false;
        if(!Pattern.matches(regex_username,user.getUsername()))
            return false;
        return true;
    }

    public static Boolean validateUserName(String username){
        return Pattern.matches(regex_username,username);
    }

}
