package com.lxc.user.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author liuxianchun
 * @date 2021/5/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistory {
    /*主键*/
    @TableId(type = IdType.AUTO)
    private BigInteger id;
    /*用户id*/
    private Integer uid;
    /*登录时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date login_time;
    /*登录ip*/
    private String login_ip;

    public LoginHistory(Integer uid, String login_ip){
        this.uid = uid;
        this.login_ip = login_ip;
    }
}
