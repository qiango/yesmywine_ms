package com.yesmywine.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.util.basic.Constants;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by SJQ on 2017/6/8.
 */
public class UserUtils {

    public static Integer getUserId(HttpServletRequest request) throws YesmywineException {
        JSONObject userJson = getUserJson(request);
        if(userJson==null){
            return null;
        }
        return userJson.getInteger("id");
    }

    public static Integer ifLoginGetId(HttpServletRequest request) throws YesmywineException {
        JSONObject userJson = getUserJson(request);
        if(userJson==null){
            ValueUtil.isError("登录超时，或用户未登录");
        }
        return userJson.getInteger("id");
    }

    public static String getUserName(HttpServletRequest request) throws YesmywineException {
        JSONObject userJson = getUserJson(request);
        if(userJson==null){
            return null;
        }
        return userJson.getString("userName");
    }
    public static String ifLoginGetName(HttpServletRequest request) throws YesmywineException {
        JSONObject userJson = getUserJson(request);
        if(userJson==null){
            ValueUtil.isError("登录超时，或用户未登录");
        }
        return userJson.getString("userName");
    }

    public static JSONObject getUserInfo(HttpServletRequest request) throws YesmywineException {

        return getUserJson(request);
    }

    private static JSONObject getUserJson(HttpServletRequest request) throws YesmywineException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String auth = httpRequest.getHeader("Authorization");
        String requestPerm = httpRequest.getHeader("RequestPerm");

        if ((auth != null) && (auth.length() > 7))
        {
            String HeadStr = auth.substring(0, 6).toLowerCase();
            if (HeadStr.compareTo("bearer") == 0)
            {

                auth = auth.substring(7, auth.length());
                Claims claims = JwtHelper.parseJWT(auth, Audience.base64Secret);

                    if ( claims!= null)
                {
                    String username = claims.getSubject();
                    String userInfo = RedisCache.get(Constants.USER_INFO+username);
                    JSONObject userJson = JSON.parseObject(userInfo);
                    return userJson;
                }
            }
        }

//        ValueUtil.isError("用户未登录或已登录超时,请重新登录");

        return null;
    }

    public static JSONObject getUserInfo(String username) {
        String userInfo = RedisCache.get(Constants.USER_INFO+username);
        JSONObject userJson = JSON.parseObject(userInfo);
        return userJson;
    }
}
