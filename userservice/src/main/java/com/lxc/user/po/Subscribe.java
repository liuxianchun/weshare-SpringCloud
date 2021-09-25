package com.lxc.user.po;

import cn.hutool.db.DaoTemplate;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liuxianchun
 * @date 2021/8/1
 * 关注
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("subscribe")
public class Subscribe {

    /*id*/
    private Long snowFlakeId;

    /*关注人id*/
    private Integer userId;

    /*被关注人id*/
    private Integer flowerId;

    /*关注时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Subscribe(Long snowFlakeId,Integer userId,Integer flowerId){
        this.snowFlakeId = snowFlakeId;
        this.userId = userId;
        this.flowerId = flowerId;
    }

}
