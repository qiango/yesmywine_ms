package com.yesmywine.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Administrator on 2017/6/5 0005.
 */
@ConfigurationProperties(prefix = "audience", locations = "classpath:jwt.properties")
public class Audience {
    public static String clientId="098f6bcd4621d373cade4e832627b4f6";
    public static String base64Secret="MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
    public static String name="restapiuser";
    public static int expiresSecond=1800;
//    public String getClientId() {
//        return clientId;
//    }
//    public void setClientId(String clientId) {
//        this.clientId = clientId;
//    }
//    public String getBase64Secret() {
//        return base64Secret;
//    }
//    public void setBase64Secret(String base64Secret) {
//        this.base64Secret = base64Secret;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public int getExpiresSecond() {
//        return expiresSecond;
//    }
//    public void setExpiresSecond(int expiresSecond) {
//        this.expiresSecond = expiresSecond;
//    }

}
