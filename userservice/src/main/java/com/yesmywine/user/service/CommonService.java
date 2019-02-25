package com.yesmywine.user.service;

import java.util.Map;

/**
 * Created by wangdiandian on 2017/5/3.
 */
public interface CommonService<T> {
    Boolean synchronous(Integer id, String url, Integer synchronous);

    Boolean synchronous(T t, String url, Integer synchronous);

    Boolean synchronous(Map<String, String> map, String url, Integer synchronous);

    Boolean synchronous(T t, String url, Integer synchronous, Map<String, String> map);

}
