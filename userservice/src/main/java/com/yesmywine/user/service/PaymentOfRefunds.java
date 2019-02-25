package com.yesmywine.user.service;

import com.yesmywine.util.error.YesmywineException;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/6/23.
 */
public interface PaymentOfRefunds {

    String payment(Map<String, String> params, Integer userId) throws YesmywineException;

    String returns(Map<String, String> params, Integer userId) throws YesmywineException;//退款

    String returnsAndPoint(Map<String, String> params, Integer userId) throws YesmywineException;//退款
}
