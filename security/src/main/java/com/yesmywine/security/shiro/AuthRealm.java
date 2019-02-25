package com.yesmywine.security.shiro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.security.redis.ShiroRedisManager;
import com.yesmywine.db.base.biz.RedisCache;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SJQ on 2017/5/25.
 */
@Component
public class AuthRealm extends AuthorizingRealm  implements Realm, InitializingBean {
    @Autowired
    private ShiroRedisManager redisManager;


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //logger.info("授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String username = (String) super.getAvailablePrincipal(principals);
        String userStr = RedisCache.get("shiro_redis_cache:"+username);
        JSONObject userJson = JSON.parseObject(userStr);
        JSONArray roles = userJson.getJSONArray("roles");
        if(roles==null){
            return info;
        }
        StringBuilder sb = new StringBuilder();
        List<String> userPerms = new ArrayList<String>();
        for (int i=0;i<roles.size();i++) {
            JSONObject role = (JSONObject)roles.get(i);
            info.addRole(role.getString("rname"));
            JSONArray perms = role.getJSONArray("perms");
            for(int j=0;j<perms.size();j++){
                JSONObject perm = (JSONObject)perms.get(j);
                String pname = perm.getString("pname");
                info.addStringPermission(pname);
                userPerms.add(pname);
            }
        }
//        redisCache.put("a72d113a-836e-4ac3-9fbc-1498b69ef163",userPerms);
//        this.redisManager.set("user_perms_"+username, userPerms);
//        System.out.println(String.join(",",userPerms);
//        this.redisManager.set("user_perms_"+username, userPerms.toString());
        return info;
    }
    //登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {


        UsernamePasswordToken utoken=(UsernamePasswordToken) token;//获取用户输入的token
        String username = utoken.getUsername();
        char[] password = utoken.getPassword();
        AuthenticationInfo info=new SimpleAuthenticationInfo(username, password,this.getClass().getName());//放入shiro.调用CredentialsMatcher检验密码
        return info;
    }

    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        super.clearCachedAuthorizationInfo(principals);
        super.clearCache(principals);
        super.clearCachedAuthenticationInfo(principals);
//        redisCache.remove(Constants.getUserModuleCacheKey(principal));
//        redisCache.remove(Constants.getUserRolesCacheKey(principal));
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
//        redisCache.clear();
        /*Cache<Object, AuthenticationInfo> cache = getAuthenticationCache();
        if (null != cache) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }*/
    }

    public void afterPropertiesSet() throws Exception {
    }


}
