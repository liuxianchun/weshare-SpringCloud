package com.lxc.file.service.api;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: liuxianchun
 * @Date: 2021/07/01
 * @Description:
 */
public interface FileResourceService {

    ResultBean publishFile(Integer uid,String username, MultipartFile file, String title, String introduction);

    void downloadFile(String objectNo, HttpServletResponse response);

    /**
     * 上传图片
     * @param file
     * @return
     */
    ResultBean uploadPicture(MultipartFile file);

}
