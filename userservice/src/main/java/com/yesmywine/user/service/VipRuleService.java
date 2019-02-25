package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.VipRule;
import com.yesmywine.util.error.YesmywineException;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/4/10.
 */
public interface VipRuleService  extends BaseService<VipRule,Integer> {
    String insert(Map<String, String> params) throws YesmywineException;
    String delete(Map<String, String> params) throws YesmywineException;


}
