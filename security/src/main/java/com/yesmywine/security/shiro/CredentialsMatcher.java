package com.yesmywine.security.shiro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.db.base.biz.RedisCache;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * Created by SJQ on 2017/5/25.
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;
        String username = utoken.getUsername();
        //获得用户输入的密码:(可以采用加盐(salt)的方式去检验)
        String inPassword = new String(utoken.getPassword());
        String userStr = RedisCache.get("shiro_redis_cache:"+username);
        JSONObject userJson = JSON.parseObject(userStr);
        String dbPassword=userJson.getString("password");
        String userId=userJson.getString("id");
        String roles=userJson.getString("roles");
//        进行密码的比对
        if(this.equals(inPassword, dbPassword)){

            return true;
        }else{
            return false;
        }


    }
}
