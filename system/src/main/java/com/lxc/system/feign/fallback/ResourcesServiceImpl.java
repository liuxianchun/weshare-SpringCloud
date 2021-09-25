package com.lxc.system.feign.fallback;

import com.lxc.common.entity.ResultBean;
import com.lxc.system.feign.api.ResourcesService;
import feign.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/29
 * @Description:
 */
@Service
public class ResourcesServiceImpl implements ResourcesService {
    @Override
    public ResultBean getResources(String type) {
        return null;
    }

    @Override
    public Response downloadFile(String objectNo) {
        return null;
    }

    @Override
    public ResultBean uploadPicture(MultipartFile file) {
        return null;
    }
}
