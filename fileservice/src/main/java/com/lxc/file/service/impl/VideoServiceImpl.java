package com.lxc.file.service.impl;

import ch.qos.logback.core.util.FileSize;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxc.common.constant.FileConst;
import com.lxc.common.constant.TimeConst;
import com.lxc.common.constant.VideoStatus;
import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.utils.IdWorker;
import com.lxc.common.utils.RedisUtil;
import com.lxc.file.dao.VideoMapper;
import com.lxc.file.pojo.Video;
import com.lxc.file.service.api.VideoService;
import com.lxc.file.utils.PublishUtil;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/29
 * @Description:
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisUtil redis;

    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    @Resource
    private VideoMapper videoMapper;

    @Autowired
    private PublishUtil publishUtil;

    @Value("${weshare.upload-dir}")
    private String uploadDir;

    private String separator = File.separator;

    @Override
    public PageBean getCheckVideo(Integer current,Integer size) {
        if (size==null||size>100)
            size = 100;
        IPage<Video> iPage = new Page<>(current, size);
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        //????????????????????????
        queryWrapper.eq("status",0);
        IPage<Video> videoPage = videoMapper.selectPage(iPage, queryWrapper);
        return new PageBean(videoPage);
    }

    @Override
    public ResultBean addVideo(MultipartFile multi_video, String posterPath, Integer uid,
                               String username, String title, String introduction) {
        log.info("??????????????????:"+multi_video.getResource().getFilename());
        //???????????????
        if (uid==null||StringUtils.isEmpty(username))
            return ResultBean.error("??????????????????");
        String video_name = multi_video.getResource().getFilename();
        if (video_name==null||!video_name.contains("."))
            return ResultBean.error("?????????????????????");
        String video_suffix = video_name.substring(video_name.lastIndexOf(".")).toUpperCase();
        if(!FileConst.VIDEO_LIST.contains(video_suffix))
            return ResultBean.error("????????????????????????");
        if(multi_video.getSize()>100* FileSize.MB_COEFFICIENT)
            return ResultBean.error("??????????????????");
        if(title.length()>50)
            return ResultBean.error("????????????");
        if(introduction.length()>300)
            return ResultBean.error("????????????");
        ResultBean resultBean = publishUtil.canPublish(uid);
        if (!resultBean.isResult())
            return resultBean;
        Map videoIdMap = idWorker.createVideoId();
        String svid = (String) videoIdMap.get("svid");
        Long vid = (Long) videoIdMap.get("vid");
        String today = DateUtil.format(new Date(), "yyyyMMdd");
        //??????????????????
        String videoDIR = "video"+separator+today;
        if(!new File(uploadDir+videoDIR).exists())
            new File(uploadDir+videoDIR).mkdirs();
        //????????????????????????
        String videoPath = videoDIR + separator + vid + video_suffix;
        try {
            multi_video.transferTo(new File(uploadDir+videoPath));
        } catch (IOException e) {
            if (!StringUtils.isEmpty(posterPath))
                new File(uploadDir+posterPath).delete();
            log.error("?????????????????????????????????,??????:"+e);
            return ResultBean.error("??????????????????");
        }
        //?????????????????????????????????
        if (StringUtils.isEmpty(posterPath)){
            try {
                FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(new File(uploadDir + videoPath));
                ff.start();
                int ffLength = ff.getLengthInFrames();
                Frame f;
                int i=0,count=0;
                while(i<ffLength){
                    f = ff.grabFrame();
                    if (i>20||i>=ffLength/2){
                        String posterDIR = uploadDir+"poster"+separator+today;
                        posterPath = "poster"+separator+today+separator+ vid+".jpg";
                        if (!new File(posterDIR).exists())
                            new File(posterDIR).mkdirs();
                        boolean res = doExecuteFrame(f, uploadDir + posterPath);
                        if (res)
                            break;
                        count++;
                        if (count>3)
                            throw new FrameGrabber.Exception("??????????????????");
                    }
                    i++;
                }
            } catch (FrameGrabber.Exception e) {
                log.error("??????????????????,??????:"+e);
                new File(uploadDir+videoPath).delete();
                return ResultBean.error("??????????????????");
            }
        }
        Video video = new Video();
        video.setSnowFlakeId(vid);
        video.setSvid(svid);
        video.setUid(uid);
        video.setUsername(username);
        video.setTitle(title);
        video.setIntroduction(introduction);
        video.setVideoSize(multi_video.getSize());
        video.setVideoPath(videoPath);
        video.setPosterPath(posterPath);
        int insert = videoMapper.insert(video);
        if(insert>0)
            return ResultBean.success("????????????,????????????");
        else{
            log.error("???????????????????????????");
            new File(uploadDir+videoPath).delete();
            new File(uploadDir+posterPath).delete();
            return ResultBean.error("??????????????????");
        }
    }

    @Override
    public ResultBean addPoster(MultipartFile multi_poster) {
        String poster_name = multi_poster.getResource().getFilename();
        if(poster_name==null||!poster_name.contains("."))
            return ResultBean.error("??????????????????");
        String poster_suffix = poster_name.substring(poster_name.lastIndexOf(".")).toUpperCase();
        if (!FileConst.IMG_LIST.contains(poster_suffix))
            return ResultBean.error("????????????????????????");
        if(multi_poster.getSize()>5*FileSize.MB_COEFFICIENT)
            return ResultBean.error("??????????????????");
        String today = DateUtil.format(new Date(), "yyyyMMdd");
        //??????????????????
        String posterDIR = "poster" + separator + today;
        if (!new File(uploadDir+posterDIR).exists())
            new File(uploadDir+posterDIR).mkdirs();
        long snowflakeId = idWorker.snowId();
        String posterPath = posterDIR + separator + snowflakeId + poster_suffix;
        try {
            multi_poster.transferTo(new File(uploadDir+posterPath));
        } catch (IOException e) {
            log.error("??????????????????,??????:"+e);
            return ResultBean.error("??????????????????");
        }
        return ResultBean.success("??????????????????",posterPath);
    }

    @Override
    public ResultBean deleteVideo(Video video) {
        return null;
    }

    @Override
    public ResultBean updateVideo(Video video) {
        return null;
    }

    @Override
    public ResultBean getVideo(String svid) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("svid.keyword", svid))
                .build();
        List<Video> videos = esTemplate.queryForList(query, Video.class);
        return ResultBean.success("????????????",videos.size()>0?videos.get(0):null);
    }


    @Override
    public ResultBean checkVideo(Long snowFlakeId, Integer status,String reason) {
        if (!VideoStatus.CHECKED.equals(status)&&!VideoStatus.FAIL_CHECK.equals(status))
            return ResultBean.error("???????????????");
        if (VideoStatus.FAIL_CHECK.equals(status)&&StringUtils.isEmpty(reason))
            return ResultBean.error("????????????????????????????????????");
        if (VideoStatus.CHECKED.equals(status))
            reason = null;
        if (reason!=null&&reason.length()>200)
            return ResultBean.error("??????????????????");
        Video video = getVideoBySnowFlakeId(snowFlakeId);
        if (video==null)
            return ResultBean.error("???????????????");
        if (video.getStatus().equals(VideoStatus.CHECKED)||video.getStatus().equals(VideoStatus.FAIL_CHECK))
            return ResultBean.error("????????????");
        video.setStatus(status);
        video.setReason(reason);
        video.setTimestamp(video.getCreateTime().getTime());
        boolean success = redisUpdateVideo(video);
        if (success){
            //??????????????????es
            video.init();
            IndexQuery indexQuery = new IndexQueryBuilder().withObject(video).build();
            esTemplate.index(indexQuery);
            return ResultBean.success("????????????");
        }
        else
            return ResultBean.error("????????????");
    }

    //????????????????????????
    private static boolean doExecuteFrame(Frame f, String targetFilePath) {
        String imgFormat = "jpg";
        if (null == f || null == f.image) {
            return false;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(f);
        File output = new File(targetFilePath);
        try {
            return ImageIO.write(bi, imgFormat, output);
        } catch (IOException e) {
            log.error("??????????????????????????????,??????:"+e);
        }
        return false;
    }

    private Video getVideoBySnowFlakeId(Long snowFlakeId) {
        Video video = (Video) redis.get("video:" + snowFlakeId);
        if (video==null){
            video = videoMapper.selectById(snowFlakeId);
            if (video!=null)
                redis.set("video:"+snowFlakeId,video);
        }
        return video;
    }

    private boolean redisUpdateVideo(Video video){
        int update = videoMapper.updateById(video);
        if (update>0){
            redis.set("video:" + video.getSnowFlakeId(), video, TimeConst.MONTH);
            return true;
        }else
            return false;
    }

}
