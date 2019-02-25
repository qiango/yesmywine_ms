package com.yesmywine.sso.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.sso.bean.OrderPointRule;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by ${shuang} on 2017/7/11.
 */
public interface OrderPointRuleService extends BaseService<OrderPointRule,Integer> {
    String allocation(String rule) throws YesmywineException;//配置

    String  setRule(Integer id) throws YesmywineException;//获取当前应用配置
    String  getPoint(Double money) throws  YesmywineException;//换算积分
}
