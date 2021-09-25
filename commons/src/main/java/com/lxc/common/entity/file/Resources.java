package com.lxc.common.entity.file;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @author liuxianchun
 * @date 2021/6/16
 * 静态资源
 */
@TableName("resources")
@Data
@ApiModel("系统资源")
public class Resources {

    /*主键id*/
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*资源类型id*/
    @TableField("type_id")
    private Integer typeId;

    /*资源类型*/
    @TableField("type")
    private String type;

    /*资源类型名*/
    @TableField("type_name")
    private String typeName;

    /*文件名*/
    @TableField("name")
    private String name;

    /*文件路径*/
    @TableField("path")
    private String path;

    /*创建时间*/
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /*修改时间*/
    @TableField("modify_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

}
