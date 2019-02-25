package com.yesmywine.user.service.impl;


import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.user.service.CommonService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by light on 2017/4/7.
 */
@Service
public class                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       CommonServiceImpl<T> implements CommonService<T> {
    @Override
    public Boolean synchronous(Integer id, String url, Integer synchronous) {
        HttpBean httpRequest = new HttpBean(url, RequestMethod.post);
        httpRequest.addParameter("id", id);
        httpRequest.addParameter("synchronous", synchronous);
        try {
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String code = ValueUtil.getFromJson(temp, "code");
            if ("200".equals(code)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public Boolean synchronous(T t, String url, Integer synchronous) {
        if (t == null) {
            return false;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(t);

                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            return false;
        }


        for (int i = 0; i < 3; i++) {
            HttpBean httpRequest = new HttpBean(url, RequestMethod.post);

            if (0 == synchronous) {
                map.remove("id");
            }
            httpRequest.addParameter("synchronous", synchronous);

            for (String key : map.keySet()) {
                httpRequest.addParameter(key, map.get(key));
            }

            try {
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String code = ValueUtil.getFromJson(temp, "code");
                if ("200".equals(code)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public Boolean synchronous(Map<String, String> map, String url, Integer synchronous) {
        HttpBean httpRequest = new HttpBean(url, RequestMethod.post);
        for (String key : map.keySet()) {
            httpRequest.addParameter(key, map.get(key));
        }
        httpRequest.addParameter("synchronous", synchronous);
        try {
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String code = ValueUtil.getFromJson(temp, "code");
            if ("200".equals(code)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public Boolean synchronous(T t, String url, Integer synchronous, Map<String, String> map) {
        if (t == null) {
            return false;
        }
        Map<String, Object> map1 = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(t);

                    map1.put(key, value);
                }
            }
        } catch (Exception e) {
            return false;
        }

        HttpBean httpRequest = new HttpBean(url, RequestMethod.post);

        for (String sky : map.keySet()) {
            map1.remove(sky);
            httpRequest.addParameter(sky, map.get(sky));
        }

        for (int i = 0; i < 3; i++) {


            if (0 == synchronous) {
                map1.remove("id");
            }
            httpRequest.addParameter("synchronous", synchronous);

            for (String key : map1.keySet()) {
                httpRequest.addParameter(key, map1.get(key));
            }
            try {
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String code = ValueUtil.getFromJson(temp, "code");
                if ("200".equals(code)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

}
