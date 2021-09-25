package com.lxc.file.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @Author: liuxianchun
 * @Date: 2021/07/02
 * @Description: 用户上传文件资源
 */
@Data
@TableName("file_resource")
@Document(indexName = "file_index")
public class FileResource {

    /*文件雪花id*/
    @Id
    @TableId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long snowFlakeId;
    /*文件上传用户id*/
    @Field(type = FieldType.Integer)
    private Integer uid;
    /*文件标题*/
    @Field(type = FieldType.Text)
    private String title;
    /*文件简介*/
    @Field(type = FieldType.Text)
    private String introduction;
    /*文件名*/
    @Field(type = FieldType.Text)
    private String name;
    /*文件路径*/
    @Field(type = FieldType.Text)
    private String path;
    /*文件大小*/
    @Field(type = FieldType.Long)
    private Long size;
    /*非数据库字段，用户名*/
    @TableField(exist = false)
    @Field(type = FieldType.Text)
    private String username;
    /*创建时间*/
    @Field(type = FieldType.Date,format = DateFormat.basic_date,pattern = "yyyyMMdd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    /*修改时间*/
    @Field(type = FieldType.Date,format = DateFormat.basic_date,pattern = "yyyyMMdd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    /*es字段，时间戳*/
    @TableField(exist = false)
    @Field(type = FieldType.Long)
    private Long timestamp;

    /*非数据库字段，浏览量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer viewNum;

    /*非数据库字段，下载量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer downloadNum;

    /*非数据库字段，收藏量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer starNum;

    /*非数据库字段，点赞量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer likeNum;

    public void init(){
        this.viewNum = 0;
        this.likeNum = 0;
        this.starNum = 0;
        this.downloadNum = 0;
        Date date = new Date();
        this.createTime = date;
        this.modifyTime = date;
        this.timestamp = date.getTime();
    }

}
