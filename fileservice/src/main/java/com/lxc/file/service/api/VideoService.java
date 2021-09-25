package com.lxc.file.service.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.file.pojo.Video;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/29
 * @Description:
 */
public interface VideoService {

    PageBean getCheckVideo(Integer current,Integer size);

    ResultBean addVideo(MultipartFile video, String posterUrl, Integer uid,String username, String title, String introduction);

    ResultBean addPoster(MultipartFile poster);

    ResultBean deleteVideo(Video video);

    ResultBean updateVideo(Video video);

    ResultBean getVideo(String svid);

    ResultBean checkVideo(Long snowFlakeId, Integer status,String reason);

}
