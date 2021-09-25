package com.lxc.common.enums;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/29
 * @Description: 文件枚举类
 */
public enum FileEnum {

    VIDEO(1,"video","视频"),
    FILE(2,"file","分享文件"),
    ARTICLE(3,"article","文章");

    /*文件类型id*/
    private Integer typeId;
    /*文件类型*/
    private String type;
    /*文件类型名*/
    private String typeName;

    FileEnum(Integer typeId,String type,String typeName){
        this.typeId = typeId;
        this.type = type;
        this.typeName = typeName;
    }

    Integer getTypeId(String type){
        for(FileEnum fileEnum:values()){
            if(fileEnum.type.equals(type))
                return fileEnum.typeId;
        }
        return null;
    }

    FileEnum getEnumByType(String type){
        for(FileEnum fileEnum:values()){
            if(fileEnum.type.equals(type))
                return fileEnum;
        }
        return null;
    }

}
