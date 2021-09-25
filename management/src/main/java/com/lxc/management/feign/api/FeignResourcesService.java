package com.lxc.management.feign.api;

import com.lxc.common.entity.ResultBean;
import com.lxc.management.feign.fallback.ResourcesServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/24
 * @Description:
 */
@Service
@FeignClient(value = "file-service",fallback = ResourcesServiceImpl.class)
public interface FeignResourcesService {

    @GetMapping("/resources/{type}")
    ResultBean getResources(@PathVariable("type") String type);

    //表单提交
    @PostMapping(value = "/resources/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultBean addResources(@RequestPart("file")MultipartFile file, @RequestParam("type") String type);

    @DeleteMapping(value = "/resources/delete")
    ResultBean deleteResources(@RequestParam("id") Integer id);

    @PostMapping(value = "/resources/update",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultBean updateResources(@RequestPart("file") MultipartFile file,@RequestParam("id") Integer id);

}
