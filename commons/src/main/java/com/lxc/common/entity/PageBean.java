package com.lxc.common.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/7/7
 * 通用页面数据类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("页面数据类")
public class PageBean {

    @ApiModelProperty("请求是否成功")
    private Boolean result;

    @ApiModelProperty("返回提示消息")
    private String message;

    @ApiModelProperty("当前页")
    private Integer currentPage;

    @ApiModelProperty("总页数")
    private Integer totalPage;

    @ApiModelProperty("总记录数")
    private Integer totalSize;

    @ApiModelProperty("每页大小")
    private Integer size;

    @ApiModelProperty("数据")
    private List data;

    public PageBean(IPage page){
        this.result = true;
        this.currentPage = (int)page.getCurrent();
        this.size = (int)page.getSize();
        this.totalSize = (int)page.getTotal();
        this.totalPage = (int)Math.ceil(this.totalSize/(this.size*1.0));
        this.data = page.getRecords();
    }

    public PageBean(List data,Integer currentPage,Integer size){
        this.result = true;
        this.currentPage = currentPage;
        this.size = size;
    }

    public static PageBean error(){
        PageBean pageBean = new PageBean();
        pageBean.result = false;
        pageBean.setCurrentPage(0);
        pageBean.setTotalPage(0);
        pageBean.setSize(0);
        pageBean.setTotalSize(0);
        return pageBean;
    }

    public static PageBean error(String message){
        PageBean pageBean = new PageBean();
        pageBean.result = false;
        pageBean.setMessage(message);
        pageBean.setCurrentPage(0);
        pageBean.setTotalPage(0);
        pageBean.setSize(0);
        pageBean.setTotalSize(0);
        return pageBean;
    }

}
