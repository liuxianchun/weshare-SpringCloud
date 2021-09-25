package com.lxc.management.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxc.management.dao.AdminMapper;
import com.lxc.management.pojo.Admin;
import com.lxc.management.pojo.RoleId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/23
 * @Description: 管理用户缓存
 */
@Component
@Slf4j
public class AdminCache {

    private ConcurrentHashMap<String, Admin> map = new ConcurrentHashMap<>();

    @Resource
    private AdminMapper adminMapper;

    @PostConstruct
    public void initAdminMap(){
        List<Admin> admins = adminMapper.selectList(new QueryWrapper<>());
        int success = 0,fail = 0;
        for(Admin admin:admins){
            if(this.map.containsKey(admin.getIp())){
                fail++;
                log.info("IP冲突,该用户无法完成注入:"+admin);
            }
            this.map.put(admin.getIp(),admin);
            success++;
        }
        log.info("数据库查询管理用户数:"+admins.size()+",注入成功:"+success+",注入失败:"+fail);
    }

    public ConcurrentHashMap<String,Admin> getAdminMap(){
        return this.map;
    }

    public boolean addAdmin(Admin admin,String token){
        if (StringUtils.isEmpty(token)||!token.contains("@"))
            return false;
        Integer uid = Integer.valueOf(token.split("@")[0]);
        Admin user = adminMapper.selectById(uid);
        //root用户可添加
        if (!RoleId.ROOT.equals(user.getRoleId()))
            return false;
        if(!this.map.containsKey(admin.getIp())) {
            int insert = adminMapper.insert(admin);
            if(insert>0){
                this.map.put(admin.getIp(),admin);
                return true;
            }
        }
        return false;
    }
}
