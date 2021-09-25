package com.lxc.system.controller;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.utils.CookieUtil;
import com.lxc.system.feign.api.LoginService;
import com.lxc.system.feign.api.ResourcesService;
import com.lxc.system.feign.api.UserService;
import feign.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author liuxianchun
 * @date 2021/6/16
 */
@RestController
@Api(tags = "获取资源")
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private UserService userService;

    @GetMapping("/{type}")
    @ApiOperation("根据资源类型获取静态资源")
    ResultBean getResources(@PathVariable("type") String type){
        return resourcesService.getResources(type);
    }

    @PostMapping("/downloadFile")
    @ApiOperation("下载文件")
    void downloadFile(@RequestParam("objectNo") String objectNo,
                                HttpServletRequest request, HttpServletResponse response){
        String token = CookieUtil.getCookieValue(request, "token", false);
        ResultBean resultBean = userService.canDownloadFile(token, objectNo);
        if (!resultBean.isResult())
            return;
        else {
            InputStream inputStream;
            try {
                Response serviceResponse = resourcesService.downloadFile(objectNo);
                inputStream = serviceResponse.body().asInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                response.setHeader("Content-Disposition",serviceResponse.headers().get("Content-Disposition")
                        .toString().replace("[","").replace("]",""));
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
                int length = 0;
                byte[] temp = new byte[1024 * 10];
                while((length = (bufferedInputStream.read(temp)))!=-1){
                    bufferedOutputStream.write(temp,0,length);
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                bufferedInputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/canDownload")
    @ApiOperation("是否可以下载文件")
    ResultBean canDownload(@RequestParam("objectNo") String objectNo,HttpServletRequest request){
        String token = CookieUtil.getCookieValue(request, "token", false);
        return userService.canDownloadFile(token,objectNo);
    }

}
