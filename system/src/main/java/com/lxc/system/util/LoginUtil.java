package com.lxc.system.util;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.utils.CookieUtil;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/27
 * @Description:
 */
public class LoginUtil {

    /*将token写入cookie*/
    public static void setToken(HttpServletRequest req, HttpServletResponse res, ResultBean resultBean){
        try{
            HashMap data = (HashMap) resultBean.getData();
            if(data!=null&&!data.isEmpty()){
                String token = (String) data.get("token");
                if(!StringUtils.isEmpty(token))
                    CookieUtil.setCookie(req,res,"token",token,-1,false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
