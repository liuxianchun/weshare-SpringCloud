package com.lxc.file.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author liuxianchun
 * @date 2021/7/3
 */
@Data
@TableName("article")
@Document(indexName = "article_index")
public class Article {

    /*雪花id*/
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long snowFlakeId;
    /*用户id*/
    @Field(type = FieldType.Integer)
    private Integer uid;
    /*文章标题*/
    @Field(type = FieldType.Text)
    private String title;
    /*文章内容*/
    @Field(type = FieldType.Text)
    private String content;
    /*创建时间*/
    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    /*修改时间*/
    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    /*es字段，时间戳*/
    @TableField(exist = false)
    @Field(type = FieldType.Long)
    private Long timestamp;

    /*非数据库字段，用户名*/
    @TableField(exist = false)
    @Field(type = FieldType.Text)
    private String username;

    /*非数据库字段，用户头像*/
    @TableField(exist = false)
    private String avatar;

    /*非数据库字段，浏览量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer viewNum;

    /*非数据库字段，点赞量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer likeNum;

    /*非数据库字段，收藏量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer starNum;

    public void init(){
        this.viewNum = 0;
        this.likeNum = 0;
        this.starNum = 0;
        Date date = new Date();
        this.createTime = date;
        this.modifyTime = date;
        this.timestamp = date.getTime();
    }

}
