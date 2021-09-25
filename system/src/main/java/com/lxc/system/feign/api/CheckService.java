package com.lxc.system.feign.api;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.system.feign.fallback.CheckServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxianchun
 * @date 2021/7/7
 */
@Service
@FeignClient(value = "file-service",contextId = "check",fallback = CheckServiceImpl.class)
public interface CheckService {

    @GetMapping("/video/getCheckVideo")
    @ApiOperation("management模块调用,获取未审核的视频")
    PageBean getCheckVideo(@RequestParam("current") Integer current, @RequestParam("size") Integer size);

    @PostMapping("/video/checkVideo")
    @ApiOperation("审核视频")
    ResultBean checkVideo(@RequestParam("snowFlakeId") Long snowFlakeId,
                          @RequestParam("status") Integer status,
                          @RequestParam("reason") String reason);

}
