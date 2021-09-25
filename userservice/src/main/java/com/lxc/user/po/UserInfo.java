package com.lxc.user.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/16
 * @Description: 用户基本信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Integer uid;
    private String username;
    private String avatar;
}
