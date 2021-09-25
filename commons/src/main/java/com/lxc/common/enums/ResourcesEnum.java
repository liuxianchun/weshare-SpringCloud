package com.lxc.common.enums;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/18
 * @Description: 资源枚举类
 */
public enum ResourcesEnum {

    CAROUSEL(1,"carousel","轮播图");

    /*资源类型id*/
    private Integer typeId;

    /*资源类型*/
    private String type;

    /*资源名*/
    private String typeName;

    ResourcesEnum(Integer typeId,String type,String typeName){
        this.typeId = typeId;
        this.type = type;
        this.typeName = typeName;
    }

    public String getType(){
        return this.type;
    }

    public String getTypeName(){
        return this.typeName;
    }

    public Integer getTypeId(){
        return this.typeId;
    }

    public static ResourcesEnum getEnumByType(String type){
        for(ResourcesEnum resourcesEnum:values()){
            if(resourcesEnum.type.contentEquals(type)){
                return resourcesEnum;
            }
        }
        return null;
    }
}
