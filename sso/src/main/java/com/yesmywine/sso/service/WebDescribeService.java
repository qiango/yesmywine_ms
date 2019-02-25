package com.yesmywine.sso.service;

import com.yesmywine.util.error.YesmywineException;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/6/15.
 */

public interface WebDescribeService {

    String insert(Map<String, String> params) throws YesmywineException;//新增网站建设
    String update(Map<String, String> params) throws YesmywineException;//新增网站建设

}
