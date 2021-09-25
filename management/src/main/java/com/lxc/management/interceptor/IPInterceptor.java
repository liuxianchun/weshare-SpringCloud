package com.lxc.management.interceptor;

import com.lxc.common.utils.CookieUtil;
import com.lxc.management.component.AdminCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxianchun
 * @date 2021/6/19
 * @Description: IP拦截
 */
@Component
public class IPInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminCache adminCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = CookieUtil.getIp(request);
        return adminCache.getAdminMap().containsKey(ip);
    }

}
