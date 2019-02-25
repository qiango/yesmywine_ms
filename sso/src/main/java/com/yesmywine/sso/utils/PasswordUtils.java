package com.yesmywine.sso.utils;

/**
 * Created by by on 2017/6/26.
 */
public class PasswordUtils {
    /**
     * 登陆名加密,  含手机和邮箱
     * @param src
     * @return
     */
    public String encodeLoginName(String src) {
        return src;
    }
    /**
     * 登陆名解密
     * @param src
     * @param appType "0" 官网,  "2" 后台
     * @params 用户ip (如无用户ip暂填本地ip)
     * @return
     */
    public String decodeLoginName(String src, String appType, String ip) {
        return src;
    }
    /**
     * 密码加密
     * @param src
     * @return
     */
    public static String encodePassword(String src) {
        return src;
    }
    /**
     * 密码解密
     * @param src
     * @param appType "0" 官网,  "2" 后台
     * @params 用户ip (如无用户ip暂填本地ip)
     * @return
     */
    public static  String decodePassword(String src, String appType, String ip) {
        return src;
    }

}
