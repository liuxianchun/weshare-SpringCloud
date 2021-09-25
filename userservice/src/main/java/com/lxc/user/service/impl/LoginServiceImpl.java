package com.lxc.user.service.impl;

import cn.hutool.core.util.IdUtil;
import com.lxc.common.constant.NumConst;
import com.lxc.common.constant.TimeConst;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.common.utils.CookieUtil;
import com.lxc.common.utils.RedisUtil;
import com.lxc.user.dao.LoginHistoryMapper;
import com.lxc.user.dao.UserMapper;
import com.lxc.user.po.LoginHistory;
import com.lxc.user.service.api.LoginService;
import com.lxc.user.service.api.MailService;
import com.lxc.user.util.MD5Util;
import com.lxc.user.util.Validator;
import com.lxc.user.util.VerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/20
 * @Description:
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private LoginHistoryMapper loginHistoryMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisUtil redis;

    @Override
    public ResultBean findAccount(String account) {
        if(userMapper.findAccount(account)>0)
            return ResultBean.error("账号已注册");
        return ResultBean.success("账号未注册");
    }

    @Override
    public ResultBean findUserName(String username) {
        if(userMapper.findUserName(username)>0)
            return ResultBean.error("用户名已使用");
        return ResultBean.success("用户名未使用");
    }

    @Override
    public ResultBean isLogin(String token) {
        if(StringUtils.isEmpty(token)||!token.contains("@"))
            return ResultBean.error("未登录");
        String uid = token.substring(0, token.indexOf("@"));
        if(!token.equals(redis.get("login:token:"+uid))){
            return ResultBean.error("未登录");
        }else{
            return ResultBean.success("已登录");
        }
    }

    @Override
    @Transactional
    public ResultBean register(HttpServletRequest req, User user) {
        if(!Validator.validateUser(user))
            return ResultBean.error("数据不合法");
        String verifyCode = (String)redis.get("register:mailCode:"+user.getAccount());
        if(verifyCode==null)
            return ResultBean.error("验证码异常");
        if (!verifyCode.equalsIgnoreCase(user.getVerifyCode()))
            return ResultBean.error("验证码错误");
        int account = userMapper.findAccount(user.getAccount());
        int userName = userMapper.findUserName(user.getUsername());
        if(account>0||userName>0)
            return ResultBean.error("账号或用户名已存在");
        String ip = CookieUtil.getIp(req);
        user.setRegisterIp(ip);
        //产生随机盐，再进行MD5加密
        String salt = IdUtil.fastSimpleUUID();
        String DBPass = MD5Util.formPassToDBPass(user.getPassword(), salt);
        user.setPassword(DBPass);
        user.setSalt(salt);
        int result = userMapper.insertUser(user);
        if(result>0){
            user = userMapper.findUser(user);
            log.info("用户"+user.getUsername()+"注册成功，账号为:"+user.getAccount());
            return ResultBean.success("注册成功",loginAction(user));
        }
        return ResultBean.error("注册失败");
    }

    @Override
    public ResultBean login(HttpServletRequest req,String loginCode, User loginUser) {
        String ip = CookieUtil.getIp(req);
        if (redis.hasKey("lock:login:passError:"+ip))
            return ResultBean.error("已被锁定,请稍后再试");
        if (loginCode == null || !loginCode.equalsIgnoreCase(loginUser.getVerifyCode()))
            return ResultBean.error("验证码错误");
        String salt = userMapper.getSaltByAccount(loginUser.getAccount());
        if(StringUtils.isEmpty(salt))
            return ResultBean.error("账号错误");
        loginUser.setPassword(MD5Util.formPassToDBPass(loginUser.getPassword(),salt));
        User user = userMapper.findUser(loginUser);
        if (user == null){
            Long error = redis.incr("login:passError:" + ip, NumConst.ONE,5*TimeConst.MINUTE);
            if (error>5){
                redis.set("lock:login:passError:"+ip,TimeConst.MINUTE);
                return ResultBean.error("输入错误次数过多,本设备被锁定1分钟");
            }
            return ResultBean.error("账号或密码错误");
        }
        //插入登录记录
        loginHistoryMapper.insert(new LoginHistory(user.getUid(),ip));
        log.info("用户登录成功:"+user.getAccount());
        return ResultBean.success("登录成功", loginAction(user));
    }

    /*退出登录*/
    @Override
    public ResultBean logout(String token){
        if (StringUtils.isEmpty(token)||!token.contains("@"))
            return ResultBean.error("未登录");
        String uid = token.split("@")[0];
        if(!token.equals(redis.get("login:token:"+uid)))
            return ResultBean.error("无效的操作");
        redis.del("login:token:"+uid);
        return ResultBean.success("退出登录成功");
    }

    /*本服务调用，弃用*/
    @Override
    @Deprecated
    public void getVerifyCode(HttpServletRequest req, HttpServletResponse res){
        HttpSession session = req.getSession();
        Object[] objs = VerifyUtil.createImage();
        session.setAttribute("verifyCode",objs[0]);
        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        res.setContentType("image/png");
        try{
            OutputStream os = res.getOutputStream();
            ImageIO.write(image, "png", os);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<byte[]> getVerifyCodeRPC(){
        Object[] objs = VerifyUtil.createImage();
        log.info("验证码:"+objs[0]);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            ImageIO.write((BufferedImage) objs[1],"png",out);
        }catch (Exception e){
            e.printStackTrace();
        }
        byte[] bytes = out.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","image/png");
        headers.add("connection","close");
        headers.add("loginCode",objs[0].toString());
        //将文件字节数组，header，状态码封装到ResponseEntity
        return new ResponseEntity<>(bytes,headers, HttpStatus.OK);
    }

    public ResultBean getMailCode(String account){
        if(StringUtils.isEmpty(account))
            return ResultBean.error("邮箱为空");
        //该账号是否已发送过邮件,防止频繁发送邮件
        if(redis.hasKey("register:mailCode:limit:"+account))
            return ResultBean.error("请求过于频繁，请稍后再试");
        redis.set("register:mailCode:limit:"+account,"limit", TimeConst.MINUTE);
        String mailCode = mailService.sendMailCode(account);
        if("err".equals(mailCode))
            return ResultBean.error("验证码发送失败");
        redis.set("register:mailCode:" + account, mailCode, 30* TimeConst.MINUTE);
        return ResultBean.success("邮箱验证码已发送,请注意查收");
    }

    /*登录动作，token存入redis并返回结果*/
    private HashMap loginAction(User user){
        String token = user.getUid()+"@"+IdUtil.fastUUID().replace("-","");
        log.info("token:"+token);
        //token存入redis，并返回
        redis.set("login:token:"+user.getUid(),token, 12* TimeConst.HOUR);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("user",user);
        return map;
    }

}
