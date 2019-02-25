package com.yesmywine.jwt;


import com.yesmywine.util.properties.PropertiesUtil;

/**
 * Created by Administrator on 2017/6/5 0005.
 */
public class Audience {
    public static String clientId = PropertiesUtil.getString("audience.clientId", "jwt.properties");
    public static String base64Secret = PropertiesUtil.getString("audience.base64Secret", "jwt.properties");
    public static String name = PropertiesUtil.getString("audience.name", "jwt.properties");
    public static int expiresSecond = Integer.valueOf(PropertiesUtil.getString("audience.expiresSecond", "jwt.properties"));
}
