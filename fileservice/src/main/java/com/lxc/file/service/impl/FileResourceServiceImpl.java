package com.lxc.file.service.impl;

import ch.qos.logback.core.util.FileSize;
import cn.hutool.core.date.DateUtil;
import com.lxc.common.constant.FileConst;
import com.lxc.common.constant.TimeConst;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.utils.IdWorker;
import com.lxc.common.utils.RedisUtil;
import com.lxc.file.dao.FileResourceMapper;
import com.lxc.file.pojo.FileResource;
import com.lxc.file.service.api.FileResourceService;
import com.lxc.file.utils.PublishUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: liuxianchun
 * @Date: 2021/07/02
 * @Description:
 */
@Service
@Slf4j
public class FileResourceServiceImpl implements FileResourceService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    @Resource
    private FileResourceMapper fileResourceMapper;

    @Autowired
    private RedisUtil redis;

    @Autowired
    private PublishUtil publishUtil;

    @Value("${weshare.file-store}")
    private String fileStore;

    @Value("${weshare.upload-dir}")
    private String uploadDir;

    private String separator = File.separator;

    @Transactional
    @Override
    public ResultBean publishFile(Integer uid,String username, MultipartFile file, String title, String introduction){
        if (uid==null|| StringUtils.isEmpty(username))
            return ResultBean.error("??????????????????");
        if (file==null||file.isEmpty())
            return ResultBean.error("??????????????????");
        if (file.getOriginalFilename()==null||!file.getOriginalFilename().contains("."))
            return ResultBean.error("??????????????????");
        if (file.getSize() > 50*FileSize.MB_COEFFICIENT)
            return ResultBean.error("????????????????????????50MB");
        if (StringUtils.isBlank(title)||StringUtils.isBlank(introduction)){
            return ResultBean.error("???????????????????????????");
        }
        if (title.length()>20||introduction.length()>200)
            return ResultBean.error("?????????????????????");
        ResultBean resultBean = publishUtil.canPublish(uid);
        if (!resultBean.isResult())
            return resultBean;
        String today = DateUtil.format(new Date(), "yyyyMMdd");
        String DIR = fileStore+"file"+separator+today;
        //??????????????????????????????
        if(!new File(DIR).exists())
            new File(DIR).mkdirs();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        long snowFlakeId = idWorker.snowId();
        String filePath = DIR+separator+snowFlakeId+suffix;
        File newFile = new File(filePath);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            log.error("??????????????????????????????,??????:"+e);
            return ResultBean.error("??????????????????");
        }
        FileResource fileResource = new FileResource();
        fileResource.init();
        fileResource.setSnowFlakeId(snowFlakeId);
        fileResource.setUid(uid);
        fileResource.setUsername(username);
        fileResource.setName(file.getOriginalFilename());
        fileResource.setTitle(title);
        fileResource.setIntroduction(introduction);
        fileResource.setPath(filePath);
        fileResource.setSize(file.getSize());
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(fileResource).build();
        String index = esTemplate.index(indexQuery);
        if (!StringUtils.isEmpty(index)){
            int insert = fileResourceMapper.insert(fileResource);
            if (insert>0)
                return ResultBean.success("??????????????????");
            else{
                log.error("???????????????????????????,snowFlakeId="+snowFlakeId);
            }
        }
        boolean delete = newFile.delete();
        log.error("??????????????????????????????:"+delete+",????????????:"+DIR+filePath);
        log.error("????????????elasticsearch??????,snowFlakeId="+snowFlakeId);
        return ResultBean.error("??????????????????");
    }

    @Override
    public void downloadFile(String objectNo, HttpServletResponse response) {
        String key = "downloadFile:" + objectNo;
        FileResource fileResource = (FileResource) redis.get(key);
        if (fileResource==null){
            fileResource = fileResourceMapper.selectById(objectNo);
            if (fileResource==null)
                return;
            redis.set(key,fileResource, TimeConst.MONTH);
        }
        File file = new File(fileResource.getPath());
        if (!file.exists()){
            log.warn("?????????????????????????????????:{}",fileResource.getPath());
            return;
        }
        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            response.setHeader("Content-Disposition","attachment; filename=" +
                    URLEncoder.encode(fileResource.getName(), StandardCharsets.UTF_8));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            int length = 0;
            byte[] temp = new byte[1024 * 10];
            while((length = bufferedInputStream.read(temp))!=-1){
                bufferedOutputStream.write(temp,0,length);
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            bufferedInputStream.close();
            inputStream.close();
        } catch (Exception e) {
            log.error("??????????????????????????????????????????",e);
        }
    }

    @Override
    public ResultBean uploadPicture(MultipartFile picture) {
        if (StringUtils.isEmpty(picture.getOriginalFilename()))
            return ResultBean.error("???????????????");
        String suffix = picture.getOriginalFilename().substring(picture.getOriginalFilename().lastIndexOf(".")).toUpperCase();
        if (!FileConst.IMG_LIST.contains(suffix))
            return ResultBean.error("??????????????????");
        String today = DateUtil.format(new Date(),"yyyyMMdd");
        long snowFlakeId = idWorker.snowId();
        String DIR = uploadDir+"picture"+separator+today;
        //??????????????????????????????
        if(!new File(DIR).exists())
            new File(DIR).mkdirs();
        String path = DIR + separator + snowFlakeId + suffix;
        File file = new File(path);
        try {
            picture.transferTo(file);
        } catch (IOException e) {
            log.error("??????????????????????????????,??????:{}",e.getMessage());
            return ResultBean.error("??????????????????");
        }
        String relativePath = "picture"+separator+today+separator + snowFlakeId + suffix;
        return ResultBean.success("??????????????????",relativePath);
    }
}
