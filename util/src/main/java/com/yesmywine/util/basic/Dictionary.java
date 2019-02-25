package com.yesmywine.util.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.properties.PropertiesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SJQ on 2017/4/13.
 */
public class Dictionary {
    public static final String MALL_HOST = PropertiesUtil.getString("mallHost", "dictionary.properties");

    public static final String PAAS_HOST = PropertiesUtil.getString("paasHost", "dictionary.properties");

    public static final String TOKEN_KEY = PropertiesUtil.getString("tokenKey", "dictionary.properties");

    public static final String REIDS_HOST = PropertiesUtil.getString("redisHost", "dictionary.properties");

    public static final String OMS_HOST = PropertiesUtil.getString("omsHost", "dictionary.properties");

    public static final String DIC_HOST = PropertiesUtil.getString("dicHost", "dictionary.properties");

    public static final String PHONE_MESSAGE = PropertiesUtil.getString("smsHost", "dictionary.properties");

    public static final String  PHONE_MODEL = PropertiesUtil.getString("phoneModel", "dictionary.properties");

    public static final String  LOG_PATH = PropertiesUtil.getString("logPath", "dictionary.properties");

    public static final String  SOLR_SERVER= PropertiesUtil.getString("solrServer", "dictionary.properties");

    public static Object getDictionaryValue(String sysCode){
        Map<String, Object> varietyMap = new HashMap<>();
        varietyMap.put("sysCode", sysCode);
        String result = SynchronizeUtils.getResult(DIC_HOST, "/dic/sysCode/itf", RequestMethod.get, varietyMap, null);
        List<JSONObject> list = new ArrayList<>();
        JSONArray array = JSON.parseArray(ValueUtil.getFromJson(result, "data"));
        for (int i = 0;i<array.size();i++){
            list.add(((JSONObject)array.get(i)));
        }
        return list;
    }

    public static List<String> getDicListValue(String sysCode) {
        Map<String, Object> varietyMap = new HashMap<>();
        varietyMap.put("sysCode", sysCode);
        String result = SynchronizeUtils.getResult(DIC_HOST, "/dic/sysCode/itf", RequestMethod.get, varietyMap, null);
        if(result!=null){
            String code = JSON.parseObject(result).getString("code");
            if(code!=null){
                List<String> list = new ArrayList<>();
                JSONArray array = JSON.parseArray(ValueUtil.getFromJson(result, "data"));
                for (int i = 0; i < array.size(); i++) {
                    list.add(((JSONObject) array.get(i)).getString("entityValue"));
                }
            }
        }
        return null;
    }

    public static String getDicSingleValue(String sysCode) {
        Map<String, Object> varietyMap = new HashMap<>();
        varietyMap.put("sysCode", sysCode);
        String result = SynchronizeUtils.getResult(DIC_HOST, "/dic/sysCode/itf", RequestMethod.get, varietyMap, null);
        if(result!=null){
            String code = JSON.parseObject(result).getString("code");
            if(code!=null){
                JSONArray array = JSON.parseArray(ValueUtil.getFromJson(result, "data"));
                String entityValue = ((JSONObject) array.get(0)).getString("entityValue");
                return entityValue;
            }
        }
        return null;
    }
}
