package com.yesmywine.jwt;

/**
 * Created by Administrator on 2017/6/5 0005.
 */
public class AccessToken {
    private String access_token;
    private String token_type;
    private long expires_in;
    private Object userId;
    private Object userMenusPerms;
    private String nickName;

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public String getAccess_token() {
        return access_token;
    }
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
    public String getToken_type() {
        return token_type;
    }
    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
    public long getExpires_in() {
        return expires_in;
    }
    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Object getUserMenusPerms() {
        return userMenusPerms;
    }

    public void setUserMenusPerms(Object userMenusPerms) {
        this.userMenusPerms = userMenusPerms;
    }
}
