package com.lxc.system.feign.api;

import com.lxc.common.entity.ResultBean;
import com.lxc.system.feign.fallback.ResourcesServiceImpl;
import feign.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author liuxianchun
 * @date 2021/6/16
 */
@Service
@FeignClient(value = "file-service",contextId = "resources",fallback = ResourcesServiceImpl.class)
public interface ResourcesService {

    @GetMapping("/resources/{type}")
    @ApiOperation("根据资源类型获取静态资源")
    ResultBean getResources(@PathVariable("type") String type);

    @PostMapping(value = "/file/downloadFile",consumes = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    @ApiOperation(("下载文件"))
    Response downloadFile(@RequestParam("objectNo")String objectNo);

    @PostMapping(value = "/file/uploadPicture",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("上传图片")
    public ResultBean uploadPicture(@RequestPart("picture") MultipartFile file);

}
