package com.lxc.system.feign.fallback;

import com.lxc.common.constant.VideoStatus;
import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.system.feign.api.CheckService;
import org.springframework.stereotype.Service;

/**
 * @author liuxianchun
 * @date 2021/7/7
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Override
    public PageBean getCheckVideo(Integer current, Integer size) {
        return null;
    }

    @Override
    public ResultBean checkVideo(Long snowFlakeId, Integer status,String reason) {
        return null;
    }

}
