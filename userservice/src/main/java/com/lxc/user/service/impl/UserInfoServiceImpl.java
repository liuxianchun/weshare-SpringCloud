package com.lxc.user.service.impl;

import com.lxc.common.constant.TimeConst;
import com.lxc.common.constant.UserConst;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.common.utils.RedisUtil;
import com.lxc.user.dao.UserMapper;
import com.lxc.user.mq.MQMessageSender;
import com.lxc.user.po.RoleId;
import com.lxc.user.po.UserInfo;
import com.lxc.user.service.api.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/7/7
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private MQMessageSender mqMessageSender;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redis;

    @Override
    public ResultBean isRoleCheck(String token) {
        if (StringUtils.isEmpty(token)||!token.contains("@"))
            return ResultBean.error("未登录");
        Integer uid = Integer.parseInt(token.split("@")[0]);
        User user = getUserByUid(uid);
        if (user==null)
            return ResultBean.error("未登录");
        if (RoleId.CHECK <= user.getRoleId())
            return ResultBean.success("具备审查权限");
        return ResultBean.error("没有权限");
    }

    @Override
    public User getUserByToken(String token) {
        if(StringUtils.isEmpty(token)||!token.contains("@"))
            return null;
        String uid = token.substring(0, token.indexOf("@"));
        if (StringUtils.isEmpty(uid))
            return null;
        String redisToken = (String) redis.get("login:token:" + uid);
        if(token.equals(redisToken))
            return getUserByUid(Integer.parseInt(uid));
        return null;
    }

    @Override
    public User getUserByUid(Integer uid){
        User user = (User) redis.get("user:" + uid);
        if (user == null){
            user = userMapper.getUserById(uid);
            if (user != null){
                redis.set("user:"+uid,user, TimeConst.MONTH);
            }
        }
        return user;
    }

    @Override
    public ResultBean getUserInfoByIds(List<Integer> ids) {
        List<Object> list = new LinkedList<>();
        for (Integer uid:ids){
            User user = getUserByUid(uid);
            UserInfo userInfo = new UserInfo(user.getUid(), user.getUsername(), user.getAvatar());
            list.add(userInfo);
        }
        return ResultBean.success("获取成功",list);
    }

    @Override
    public ResultBean getUserInfoById(Integer uid) {
        User user = getUserByUid(uid);
        UserInfo userInfo = new UserInfo(user.getUid(), user.getUsername(), user.getAvatar());
        return ResultBean.success("获取成功",userInfo);
    }

    @Override
    public void refreshUser(Integer uid) {
        User user = userMapper.getUserById(uid);
        if (user!=null)
            redis.set("user:"+uid,user,TimeConst.MONTH);
    }

    @Override
    public ResultBean updateAvatar(Integer uid,String relativePath) {
        User user = new User();
        user.setUid(uid);
        user.setAvatar(relativePath);
        User oldUser = getUserByUid(uid);
        if (oldUser==null){
            return ResultBean.error("用户不存在");
        }
        int update = userMapper.updateById(user);
        if (update>0){
            refreshUser(uid);
            //删除旧的头像
            mqMessageSender.sendDeleteFile(oldUser.getAvatar());
            return ResultBean.success("更新成功");
        }
        //删除未更新成功的图片
        mqMessageSender.sendDeleteFile(relativePath);
        return ResultBean.error("更新失败");
    }

    @Override
    public ResultBean updateUser(String token,User paramUser) {
        User userByToken = getUserByToken(token);
        if (userByToken==null)
            return ResultBean.error("请先登录");
        if (!UserConst.FEMALE.equals(paramUser.getSex())&&!UserConst.MALE.equals(paramUser.getSex()))
            return ResultBean.error("性别错误");
        User user = new User();
        //从token获取的uid
        user.setUid(userByToken.getUid());
        user.setBirthday(paramUser.getBirthday());
        user.setSex(paramUser.getSex());
        user.setPhone(paramUser.getPhone());
        int update = userMapper.updateById(user);
        if (update>0){
            refreshUser(user.getUid());
            return ResultBean.success("修改成功");
        }
        return ResultBean.success("信息未修改");
    }

    @Override
    public ResultBean addCoinExp(Integer uid, Integer coin,Integer scoin,Integer growth) {
        int update = userMapper.addCoinExp(uid, coin, scoin, growth);
        if (update>0)
            return ResultBean.success("更新用户硬币经验成功");
        return ResultBean.error("更新用户硬币经验成功失败");
    }

}
