package com.lxc.user.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author liuxianchun
 * @date 2021/7/25
 */
@Data
@TableName("comment")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    /*主键，雪花id*/
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long snowFlakeId;

    /*评论类型(video,file,article)*/
    private String objectType;

    /*类型的主键，雪花id*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long objectNo;

    /*用户id*/
    private Integer uid;

    /*评论内容*/
    private String content;

    /*创建时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /*修改时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    /*版本*/
    @Version
    private Integer version;

    /*用户名*/
    @TableField(exist = false)
    private String username;

    /*用户等级*/
    @TableField(exist = false)
    private Integer userLevel;

    /*用户头像*/
    @TableField(exist = false)
    private String avatar;

    public Comment(String objectType,Long objectNo,Integer uid,String content){
        this.objectType = objectType;
        this.objectNo = objectNo;
        this.uid = uid;
        this.content = content;
    }

}
