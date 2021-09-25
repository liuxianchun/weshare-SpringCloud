package com.lxc.file.service.api;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.file.Resources;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/18
 * @Description:
 */
public interface ResourcesService {

    ResultBean getResources(String type);

    ResultBean addResources(MultipartFile file,String type);

    ResultBean updateResources(MultipartFile file,Integer id);

    ResultBean deleteResources(Integer id);

}
