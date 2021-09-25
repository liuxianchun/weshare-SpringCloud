package com.lxc.management.interceptor;

import com.lxc.common.utils.CookieUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/23
 * @Description: 登录拦截
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String cookieValue = CookieUtil.getCookieValue(request, "admin", false);
        String sessionValue = (String) request.getSession().getAttribute("admin");
        //重新设置cookie过期时间
        if(cookieValue!=null&&sessionValue!=null&&cookieValue.equals(sessionValue)){
            CookieUtil.setCookie(request,response,"admin",cookieValue,3600,false);
            return true;
        }else
            return false;
    }

}
