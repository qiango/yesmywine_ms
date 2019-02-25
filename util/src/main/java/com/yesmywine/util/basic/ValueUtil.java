
package com.yesmywine.util.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.*;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by taylor on 8/7/16.
 * twitter: @taylorwang789
 */
public class ValueUtil {

    public static String defaultSuccess = "success";

    public static boolean notEmpity(Object obj) {
        if (null == obj) {
            return false;
        } else if (obj.toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isEmpity(Object obj) {
        return !notEmpity(obj);
    }


    public static <T> T coalesce(T... args) {
        for (int i = 0; i < args.length; i++) {
            if (notEmpity(args[i])) {
                return args[i];
            }
        }
        return (T) "";
    }

    public static Gson gsonExp = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    //    public static Gson gson = new Gson();
    public static Gson gson = new GsonBuilder().serializeNulls().create();

//    public static String toJson(String code, Object obj) {
//        RestJson restJson = new RestJson();
////        restJson.setCode(code);
//        restJson.setMsg(defaultSuccess);
//        restJson.setData(coalesce(obj, ""));
//        return gson.toJson(restJson);
//    }

    public static String toJson(Integer code, Object obj) {
        RestJson restJson = new RestJson();
        restJson.setCode(String.valueOf(code));
        restJson.setMsg(defaultSuccess);
        restJson.setData(coalesce(obj, ""));
        return gson.toJson(restJson);
    }

    public static String toJson(Integer code, String msg,Object obj ) {
        RestJson restJson = new RestJson();
        restJson.setCode(String.valueOf(code));
        restJson.setMsg(msg);
        restJson.setData(coalesce(obj, ""));
        return gson.toJson(restJson);
    }

    public static String toJson(Object obj) {
        try {
            RestJson restJson = new RestJson();
            restJson.setCode(String.valueOf(HttpStatus.SC_OK));
            restJson.setMsg(defaultSuccess);
            restJson.setData(coalesce(obj, ""));
//            JSONValue jsonValue = JSONMapper.toJSON(restJson);
//            String jsonStr = jsonValue.render(false);
//            return jsonStr;
            return gson.toJson(restJson);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject toJsonObject(Object obj) {
        try {
            return JSON.parseObject(gson.toJson(coalesce(obj, ""))) ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static String toString(Object obj) {
        return gson.toJson(obj);
    }


    public static String toJson(Object... objs) {
        RestJson restJson = toRestJson(objs);
        return gson.toJson(restJson);
    }

    public static String toJsonExp(Object... objs) {
        RestJson restJson = toRestJson(objs);
        return gsonExp.toJson(restJson);
    }


    public static String toError(String code, String message) {
        RestJson restJson = new RestJson();
        restJson.setCode(code);
        restJson.setData("");
        restJson.setMsg(message);
        return toString(restJson);
    }

    public static String toError(String code, String message, Object object) {
        RestJson restJson = toRestJson(object);
        restJson.setCode(code);
        restJson.setData(object);
        restJson.setMsg(message);
        return gson.toJson(restJson);
    }

    public static String toError(Integer code, String message) {
        RestJson restJson = new RestJson();
        restJson.setCode(String.valueOf(code));
        restJson.setMsg(message);
        restJson.setData("");
        return toString(restJson);
    }

    public static RestJson toRestJson(Object... objs) {
        RestJson restJson = new RestJson();
        restJson.setCode(String.valueOf(HttpStatus.SC_OK));
        restJson.setMsg("");
        Map<String, Object> map = new HashMap<>();
        boolean isOdd = true;
        String key = "";
        for (int i = 0; i < objs.length; i++) {
            if (isOdd) {
                key = objs[i].toString();
                isOdd = false;
            } else {
                if (notEmpity(objs[i])) {
                    map.put(key, objs[i]);
                } else {
                    map.put(key, new JsonObject());
                }
                isOdd = true;
            }
        }
        restJson.setData(map);
        return restJson;
    }


    private static JsonParser jsonParser = new JsonParser();

    public static JsonElement getFromJson(String json) {
        JsonElement origin = jsonParser.parse(json);
        return origin;
    }

    public static <T> T getFromJson(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

    public static String getFromJson(String json, Object... args) {
        JsonObject origin = jsonParser.parse(json).getAsJsonObject();
        for (int i = 0; i < args.length; i++) {
            if ((i + 1) == args.length) {
                String returnString = origin.get(args[i].toString()).toString();
                if (returnString.startsWith("\"")) {
                    return returnString.substring(1, returnString.length() - 1);
                } else {
                    return returnString;
                }
            } else {
                if (args[i].getClass().equals(Integer.class)) {
                    origin = origin.getAsJsonArray().get(Integer.valueOf(args[i].toString())).getAsJsonObject();
                }
                if (args[i].getClass().equals(String.class)) {
                    origin = origin.getAsJsonObject(args[i].toString());
                }
            }
        }
        return origin.toString();
    }


    public static void verify(Object param) throws YesmywineException {
        if (isEmpity(param)) {
            throw new YesmywineException("500","缺少必填参数");
        }
    }


    public static void verify(Object param, String errorCode) throws YesmywineException {
        if (isEmpity(param)) {
            throw new YesmywineException("500",errorCode+"为空");
        }
    }

    public static void verifyGoods(Object param, String errorCode) throws YesmywineException {
        if (isEmpity(param)) {
            throw new YesmywineException("500",errorCode+"为空"+","+"不可申请上下架");
        }
    }

    public static void verify(Object param, String errorCode, String errorMsg) throws YesmywineException {
        if (isEmpity(param)) {
            throw new YesmywineException(errorCode, errorMsg);
        }
    }

    public static void verify(Map<String, String> param, String[] keys) throws YesmywineException {
        for (int i = 0; i < keys.length; i++) {
            ValueUtil.verify(param.get(keys[i]), "500", keys[i] + "为空");
        }
    }


    public static void verify(boolean wanna, boolean condition, String errorCode) throws Exception {
        if (isEmpity(condition)) {
            throw new Exception(errorCode);
        } else {
            if (wanna != condition) {
                throw new Exception(errorCode);
            }
        }
    }

    /*
    *@Author Gavin
    *@Description 验证对象不存在，直接返回不存在此对象
    *@Date 2017/3/10 16:33
    *@Email gavinsjq@sina.com
    *@Params
    */
    public static void verifyNotExist(Object object,String msg)throws YesmywineException {
        if(null == object){
            isError(msg);
        }
    }

    /*
    *@Author Gavin
    *@Description 验证对象已存在，直接返回对象已存在
    *@Date 2017/3/10 16:34
    *@Email gavinsjq@sina.com
    *@Params
    */
    public static void verifyExist(Object object,String msg)throws YesmywineException {
        if(null!=object){
            isError(msg);;
        }
    }

    /*
    *@Author Gavin
    *@Description 校验验证码是否相等
    *@Date 2017/3/10 16:34
    *@Email gavinsjq@sina.com
    *@Params
    */
    public static Boolean verifyIsTrue(String origin,String now)throws YesmywineException {
        if(origin.equals(now)){
            return true;
        }else{
            return false;
        }
    }

    public static void isError(String message)throws YesmywineException {
            throw new YesmywineException("500",message);
    }
}

