package com.lxc.management.feign.fallback;

import com.lxc.common.entity.ResultBean;
import com.lxc.management.feign.api.FeignResourcesService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/24
 * @Description:
 */
@Service
public class ResourcesServiceImpl implements FeignResourcesService {
    @Override
    public ResultBean getResources(String type) {
        return null;
    }

    @Override
    public ResultBean addResources(MultipartFile file, String type) {
        return null;
    }

    @Override
    public ResultBean deleteResources(Integer id) {
        return null;
    }

    @Override
    public ResultBean updateResources(MultipartFile file, Integer id) {
        return null;
    }
}
