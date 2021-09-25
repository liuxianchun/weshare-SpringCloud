package com.lxc.user.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author liuxianchun
 * @date 2021/1/25
 * MD5工具类
 */
public class MD5Util {

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "weshare";

    /*第一次加密,前端进行加密传到后端*/
    public static String inputPassToFormPass(String inputPass){
        String str = salt.substring(0,1) + salt.substring(3,4)
                + inputPass + salt.substring(6,7) + salt.substring(5,6);
        return md5(str);
    }

    /*第二次加密，后端到数据库前*/
    public static String formPassToDBPass(String formPass, String salt){
        String str = salt.substring(0,1) + salt.substring(3,4)
                + formPass + salt.substring(6,7) + salt.substring(5,6);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt){
        String formPass = inputPassToFormPass(inputPass);
        return formPassToDBPass(formPass,salt);
    }

    public static void main(String[] args) {
        System.out.println(md5("whlxc123er"));
        System.out.println(inputPassToFormPass("lxc123"));
        System.out.println(formPassToDBPass("af664c2e90985432cccdd2ec7937dc92","weshare"));
        System.out.println(inputPassToDBPass("lxc123","weshare"));
    }

}
