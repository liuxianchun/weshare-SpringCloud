package com.lxc.common.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.checkerframework.checker.formatter.qual.Format;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/13
 * @Description:
 */
@Data
@ApiModel("用户信息")
public class User implements Serializable {

    /*数据库字段*/

    /*用户id*/
    @TableId(type = IdType.AUTO)
    private Integer uid;
    /*账号*/
    private String account;
    /*用户名*/
    private String username;
    /*密码*/
    private String password;
    /*用户角色*/
    private Integer roleId;
    /*头像*/
    private String avatar;
    /*硬币*/
    private Integer coin;
    /*s币*/
    private Integer scoin;
    /*成长值*/
    private Integer growth;
    /*手机号*/
    private Long phone;
    /*性别,M(男),F(女)*/
    private String sex;
    /*出生日期*/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date birthday;
    /*注册日期*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date registerTime;
    /*注册ip*/
    private String registerIp;
    /*随机盐*/
    private String salt;
    /*修改时间*/
    private Date modifyTime;

    /*校验字段*/

    /*确认密码*/
    @TableField(exist = false)
    private String checkPass;
    /*验证码*/
    @TableField(exist = false)
    private String verifyCode;

}
