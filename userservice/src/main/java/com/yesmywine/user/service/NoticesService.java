package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.Notices;
import com.yesmywine.util.error.YesmywineException;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/6/30.
 */
public interface NoticesService extends BaseService<Notices, Integer> {

    String create(Map<String, String> params,Integer userId) throws YesmywineException;//创建消息
}
