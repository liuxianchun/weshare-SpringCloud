package com.lxc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxc.common.constant.NumConst;
import com.lxc.common.constant.TimeConst;
import com.lxc.common.entity.Nums;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.CommentType;
import com.lxc.common.entity.user.User;
import com.lxc.common.utils.IdWorker;
import com.lxc.common.utils.RedisUtil;
import com.lxc.user.dao.*;
import com.lxc.user.mq.MQMessageSender;
import com.lxc.user.po.*;
import com.lxc.user.service.api.SubscribeService;
import com.lxc.user.service.api.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/8/1
 */
@Service
@Slf4j
public class SubscribeServiceImpl implements SubscribeService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisUtil redis;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MQMessageSender mqMessageSender;

    @Resource
    private SubscribeMapper subscribeMapper;

    @Resource
    private UserMapper userMapper;


    /**
     * 关注，uid关注人，flowerId被关注人
     *
     * @param uid
     * @param flowerId
     * @param status true:关注,false:取消关注
     * @return
     */
    @Override
    public ResultBean subscribe(Integer uid, Integer flowerId, Boolean status) {
        if (uid==null||flowerId==null||status==null)
            return ResultBean.error("参数不全");
        List flowers = getSubscribeListByUid(uid);
        //新增关注
        if (status){
            if (flowers.size()>=1000)
                return ResultBean.error("最多关注1000名用户");
            if (flowers.contains(flowerId))
                return ResultBean.error("已关注");
            long snowFlakeId = idWorker.snowId();
            int insert = subscribeMapper.insert(new Subscribe(snowFlakeId, uid, flowerId));
            if (insert>0){
                //刷新redis缓存
                refreshSubscribe(uid);
                return ResultBean.success("关注成功");
            }
            else
                return ResultBean.error("关注失败");
        }else{
            QueryWrapper<Subscribe> queryWrapper = new QueryWrapper<Subscribe>()
                    .eq("user_id",uid).eq("flower_id",flowerId);
            int delete = subscribeMapper.delete(queryWrapper);
            if (delete>0){
                //刷新redis缓存
                refreshSubscribe(uid);
                return ResultBean.success("取消关注成功");
            }else
                return ResultBean.error("取消关注失败");
        }
    }

    @Override
    public ResultBean getSubscribeList(Integer uid) {
        return ResultBean.success("获取成功",getSubscribeListByUid(uid));
    }

    //获取用户的关注列表
    private List getSubscribeListByUid(Integer uid){
        List list = new ArrayList();
        if (uid==null)
            return list;
        if (redis.hasKey("user:subscribe:"+uid)){
            list =  (List) redis.get("user:subscribe:"+uid);
        }else{
            QueryWrapper<Subscribe> queryWrapper = new QueryWrapper<Subscribe>()
                    .eq("user_id",uid).select("flower_id");
            list =  subscribeMapper.selectObjs(queryWrapper);
            redis.set("user:subscribe:"+uid,list, 7*TimeConst.DAY);
        }
        return list;
    }

    /**
     * 获取关注列表详细信息
     *
     * @param token
     * @return
     */
    @Override
    public ResultBean getSubscribeDetail(String token) {
        User userByToken = userInfoService.getUserByToken(token);
        if (userByToken==null)
            return ResultBean.error("请先登录");
        List<Integer> ids = getSubscribeListByUid(userByToken.getUid());
        List<User> subscribeList = new ArrayList<>();
        for (Integer id:ids){
            User user = userInfoService.getUserByUid(id);
            subscribeList.add(user);
        }
        return ResultBean.success("获取成功",subscribeList);
    }

    private void refreshSubscribe(Integer uid){
        QueryWrapper<Subscribe> queryWrapper = new QueryWrapper<Subscribe>()
                .eq("user_id",uid).select("flower_id");
        List list =  subscribeMapper.selectObjs(queryWrapper);
        redis.set("user:subscribe:"+uid,list, 7*TimeConst.DAY);
    }

    @Override
    public ResultBean like(Integer uid, String objectNo, String objectType,Boolean status) {
        if (uid==null|| StringUtils.isEmpty(objectNo)||!CommentType.list().contains(objectType)||status==null)
            return ResultBean.error("参数不全或错误");
        String key = "user:like:"+objectType+":"+uid;
        if (!redis.sHasKey(key,objectNo)){
            //没有点赞记录且需要点赞
            if (status){
                redis.sSet(key,objectNo);
                redis.hincr(objectType+":num:"+objectNo,"likeNum",1);
            }
        }else{
            //有点赞记录且取消点赞
            if (!status){
                redis.sDel(key,objectNo);
                redis.hdecr(objectType+":num:"+objectNo,"likeNum",1);
            }
        }
        return status?ResultBean.success("点赞成功"):ResultBean.success("取消点赞成功");
    }

    @Override
    public ResultBean star(Integer uid, String objectNo, String objectType, Boolean status) {
        if (uid==null|| StringUtils.isEmpty(objectNo)||!CommentType.list().contains(objectType)||status==null)
            return ResultBean.error("参数不全或错误");
        String key = "user:star:"+objectType+":"+uid;
        if (!redis.sHasKey(key,objectNo)){
            //没有收藏记录且需要收藏
            if (status){
                redis.sSet(key,objectNo);
                redis.hincr(objectType+":num:"+objectNo,"starNum",1);
            }
        }else{
            //有收藏记录且取消收藏
            if (!status){
                redis.sDel(key,objectNo);
                redis.hdecr(objectType+":num:"+objectNo,"starNum",1);
            }
        }
        return status?ResultBean.success("收藏成功"):ResultBean.success("取消收藏成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultBean coin(Integer uid, String objectNo,Integer autherId) {
        if (uid==null||StringUtils.isEmpty(objectNo))
            return ResultBean.error("参数缺失");
        try{
            //完成硬币扣除并增加成长值
            int update = userMapper.payCoin(uid);
            if (update==0)
                return ResultBean.error("硬币不足");
            redis.sSet("user:coin:"+uid,objectNo);
            //刷新redis
            userInfoService.refreshUser(uid);
            //增加视频投币数
            String key = "video:num:"+objectNo;
            double coinNum = redis.hincr(key, "coinNum", NumConst.ONE);
            //每获得5次投币获得1个硬币，上限100个
            if (coinNum%5==0&&coinNum<=500&&autherId!=null){
                mqMessageSender.sendUserExp(autherId,NumConst.ONE,NumConst.ZERO,NumConst.ZERO);
            }
            //每获得1次投币,增加2点成长值,上限1000点
            if (coinNum<=500&&autherId!=null){
                mqMessageSender.sendUserExp(autherId,NumConst.ZERO,NumConst.ZERO,NumConst.TWO);
            }
        }catch (Exception e){
            log.error("出现异常:"+e);
            return ResultBean.error("投币失败");
        }
        return ResultBean.success("投币成功,成长值+5");
    }

    @Override
    public ResultBean getNum(String type, String objectNo, Integer uid) {
        if (!CommentType.list().contains(type)||StringUtils.isEmpty(objectNo))
            return ResultBean.error("参数错误");
        Nums nums = new Nums();
        Map redisNums = redis.hmget(type+":num:"+objectNo);
        if (redisNums==null||redisNums.isEmpty())
            return ResultBean.success("获取成功",nums);
        nums = Nums.Map2Nums(redisNums);
        if (uid!=null){
            if (redis.sHasKey("user:like:"+type+":"+uid,objectNo))
                nums.setLikeStatus(true);
            if (redis.sHasKey("user:star:"+type+":"+uid,objectNo))
                nums.setStarStatus(true);
            if (redis.sHasKey("user:coin:"+uid,objectNo))
                nums.setCoinStatus(true);
        }
        return ResultBean.success("获取成功",nums);
    }

    @Override
    public ResultBean addView(String objectNo, String objectType, String ip) {
        //是否有锁
        String lock = "lock:view:"+ip+":"+objectType+":"+objectNo;
        if (redis.hasKey(lock))
            return ResultBean.error("浏览间隔时间过短");
        //加锁，5分钟内浏览无效
        redis.set(lock,"lock",5*TimeConst.MINUTE);
        redis.hincr(objectType+":num:"+objectNo,"viewNum",1);
        //记录需要增加浏览量的数据
        redis.set("temp:count:"+objectType+":"+objectNo,objectNo,15*TimeConst.MINUTE);
        return ResultBean.success("已查看");
    }

    @Override
    public ResultBean canDownloadFile(String token, String objectNo) {
        if (StringUtils.isEmpty(objectNo))
            return ResultBean.error("参数缺失");
        User user = userInfoService.getUserByToken(token);
        if (user==null)
            return ResultBean.error("请先登录");
        String key = "user:download:"+user.getUid();
        if (redis.sHasKey(key,objectNo))
            return ResultBean.success("允许下载");
        else{
            int update = userMapper.payScoin(user.getUid(),1);
            if (update>0){
                redis.sSet(key,objectNo);
                //增加文件下载数
                String key2 = "file:num:"+objectNo;
                redis.hincr(key2,"downloadNum",1);
                return ResultBean.success("允许下载");
            }else
                return ResultBean.error("s币余额不足");
        }
    }
}
