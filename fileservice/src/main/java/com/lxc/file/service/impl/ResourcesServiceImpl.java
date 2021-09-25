package com.lxc.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.lxc.common.constant.FileConst;
import com.lxc.common.constant.TimeConst;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.file.Resources;
import com.lxc.common.enums.ResourcesEnum;
import com.lxc.common.utils.RedisUtil;
import com.lxc.file.dao.ResourcesMapper;
import com.lxc.file.service.api.ResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/18
 * @Description:
 */
@Service
@Slf4j
public class ResourcesServiceImpl implements ResourcesService {

    @Value("${weshare.upload-dir}")
    private String uploadDir;

    @Resource
    private ResourcesMapper resourcesMapper;

    @Autowired
    private RedisUtil redis;

    private final String separator = File.separator;

    @Override
    public ResultBean getResources(String type) {
        ResourcesEnum resourcesEnum = ResourcesEnum.getEnumByType(type);
        if(resourcesEnum==null)
            return ResultBean.error("资源类型不存在");
        List<Resources> resources;
        String key = "file:resources:"+type;
        if(redis.hasKey(key))
            resources = (List<Resources>)redis.get(key);
        else{
            HashMap<String, Object> map = new HashMap<>();
            map.put("type_id",resourcesEnum.getTypeId());
            resources = resourcesMapper.selectByMap(map);
            redis.set(key,resources, TimeConst.MONTH);
        }
        return ResultBean.success("成功",resources);
    }

    @Override
    public ResultBean addResources(MultipartFile file,String type) {
        ResourcesEnum resourcesEnum = ResourcesEnum.getEnumByType(type);
        if(resourcesEnum==null)
            return ResultBean.error("资源类型错误");
        String sourceName = file.getResource().getFilename();
        if (StringUtils.isEmpty(sourceName)||!sourceName.contains("."))
            return ResultBean.error("文件名非法");
        String suffix = sourceName.substring(sourceName.lastIndexOf(".")).toUpperCase();
        if(!FileConst.IMG_LIST.contains(suffix))
            return ResultBean.error("文件格式错误");
        //path为存放的相对路径
        String dir = uploadDir+"resources"+separator+type+separator;
        File DIR = new File(dir);
        if(!DIR.exists())
            DIR.mkdirs();
        String name = UUID.fastUUID().toString().replace("-","")+ suffix;
        String path = "resources"+separator+type+separator+name;
        //上传文件到服务器
        try {
            file.transferTo(new File(uploadDir+path));
            Resources resources = new Resources();
            resources.setType(type);
            resources.setTypeId(resourcesEnum.getTypeId());
            resources.setTypeName(resourcesEnum.getTypeName());
            resources.setPath(path);
            resources.setName(name);
            int insert = resourcesMapper.insert(resources);
            if (insert>0){
                updateRedisResources(resourcesEnum.getType());
                return ResultBean.success("新增成功");
            }else{
                boolean result = FileUtil.del(uploadDir+path);
                if(!result)
                    log.warn("文件删除失败,路径:"+path);
                return ResultBean.error("新增失败");
            }
        }catch (Exception e){
            FileUtil.del(uploadDir+path);
            log.error("资源文件上传失败,原因:"+e.getCause());
            return ResultBean.error("新增失败");
        }
    }

    @Override
    public ResultBean updateResources(MultipartFile file,Integer id) {
        Resources resources = resourcesMapper.selectById(id);
        if (resources==null)
            return ResultBean.error("资源不存在");
        String sourceName = file.getResource().getFilename();
        if (StringUtils.isEmpty(sourceName)||!sourceName.contains("."))
            return ResultBean.error("文件名非法");
        String suffix = sourceName.substring(sourceName.lastIndexOf(".")).toUpperCase();
        if(!FileConst.IMG_LIST.contains(suffix))
            return ResultBean.error("文件格式错误");
        String newName = UUID.fastUUID().toString().replace("-", "") + suffix;
        String dir = uploadDir+"resources"+separator+resources.getType()+separator;
        File oldFile = new File(uploadDir + resources.getPath());
        try {
            if (new File(dir).exists())
                new File(dir).mkdirs();
            file.transferTo(new File(dir+newName));
        } catch (IOException e) {
            log.error("资源文件上传失败,原因:"+e.getCause());
            return ResultBean.error("文件上传失败");
        }
        //文件上传成功，重新设置资源，删除旧的文件
        resources.setName(newName);
        resources.setPath("resources"+separator+resources.getType()+separator+newName);
        int updated = resourcesMapper.updateById(resources);
        if(updated>0){
            updateRedisResources(resources.getType());
            oldFile.delete();
            return ResultBean.success("更新成功");
        }
        else{
            return ResultBean.error("更新失败");
        }
    }

    @Override
    public ResultBean deleteResources(Integer id) {
        Resources resources = resourcesMapper.selectById(id);
        if (resources==null)
            return ResultBean.error("资源不存在");
        String relativePath = resources.getPath();
        File file = new File(uploadDir + relativePath);
        file.delete();
        int count = resourcesMapper.deleteById(id);
        if(count>0){
            updateRedisResources(resources.getType());
            return ResultBean.success("删除成功");
        }
        return ResultBean.error("删除失败");
    }

    /**
     * 根据资源类型更新redis
     * @param type
     */
    private void updateRedisResources(String type){
        String key = "file:resources:"+type;
        HashMap<String, Object> map = new HashMap<>();
        map.put("type",type);
        List<Resources> resources = resourcesMapper.selectByMap(map);
        redis.set(key,resources, TimeConst.MONTH);
    }

}
