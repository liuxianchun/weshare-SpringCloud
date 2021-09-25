package com.lxc.file.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/29
 * @Description: 视频
 */
@TableName("video")
@Data
@Document(indexName = "video_index")
public class Video {

    /*雪花id，数字
    * js中的long比java中的long位数少，需要转化为String
    * */
    @TableId
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long snowFlakeId;
    /*视频svid，字符串*/
    @Field(type = FieldType.Keyword)
    private String svid;
    /*视频发布者uid*/
    @Field(type = FieldType.Integer)
    private Integer uid;
    /*用户名*/
    @TableField
    @Field(type = FieldType.Text)
    private String username;
    /*视频相对路径*/
    @Field(type = FieldType.Text)
    private String videoPath;
    /*封面相对路径*/
    @Field(type = FieldType.Text)
    private String posterPath;
    /*视频标题*/
    @Field(type = FieldType.Text)
    private String title;
    /*视频简介*/
    @Field(type = FieldType.Text)
    private String introduction;
    /*视频状态(0:(默认)未审核,1:审核中,2:正常)*/
    @Field(type = FieldType.Integer)
    private Integer status;
    /*视频大小*/
    @Field(type = FieldType.Long)
    private Long videoSize;
    /*原因*/
    private String reason;
    /*创建时间*/
    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    /*修改时间*/
    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;
    /*版本号，乐观锁*/
    @Version
    @Field(type = FieldType.Integer)
    private Integer version;

    /*es字段，时间戳*/
    @TableField(exist = false)
    @Field(type = FieldType.Long)
    private Long timestamp;

    /*非数据库/es字段，用户头像*/
    @TableField(exist = false)
    private String avatar;

    /*非数据库字段，播放量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer viewNum;

    /*非数据库字段，投币量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer coinNum;

    /*非数据库字段，点赞量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer likeNum;

    /*非数据库字段，收藏量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer starNum;

    /*非数据库字段，弹幕量*/
    @TableField(exist = false)
    @Field(type = FieldType.Integer)
    private Integer danmuNum;

    public void init(){
        this.viewNum = 0;
        this.likeNum = 0;
        this.starNum = 0;
        this.coinNum = 0;
        this.danmuNum = 0;
    }

}
