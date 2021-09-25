package com.lxc.system.feign.fallback;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.system.feign.api.PublishService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/30
 * @Description:
 */
@Service
public class PublishServiceImpl implements PublishService {

    @Override
    public ResultBean addPoster(MultipartFile file) {
        return null;
    }

    @Override
    public ResultBean addVideo(MultipartFile video, String posterUrl, Integer uid,String username, String title, String introduction) {
        return null;
    }

    @Override
    public ResultBean publishFile(MultipartFile file,Integer uid,String username,String title,String introduction) {
        return null;
    }

    @Override
    public ResultBean publishArticle(Integer uid,String username, String title, String content) {
        return null;
    }
}
