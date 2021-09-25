package com.lxc.common.entity.user;

import java.util.Arrays;
import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/7/25
 * 评论类型
 */
public class CommentType {

    public static final String VIDEO = "video";

    public static final String FILE = "file";

    public static final String ARTICLE = "article";

    public static List list(){
        return Arrays.asList(VIDEO,FILE,ARTICLE);
    }

}
