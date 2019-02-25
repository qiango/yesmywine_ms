package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.ReceivingAddress;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/5/3.
 */
public interface ReceivingAddressService extends BaseService<ReceivingAddress, Integer> {
    String create(Map<String, String> param,Integer userId) throws YesmywineException;

    String delete(Integer id) throws YesmywineException;

    String updateSave(Map<String, String> param) throws YesmywineException;

    ReceivingAddress showOne(Integer id) throws YesmywineException;

    String acquiesce(Integer id,Integer userId) throws YesmywineException;

    ReceivingAddress showOneDefault(Integer userId) throws YesmywineException;

    List<ReceivingAddress> findReceivingAddressAll(Integer userId)throws YesmywineException;

}
