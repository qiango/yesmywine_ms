package com.yesmywine.goods.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entityProperties.PropertiesValue;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by wangdiandian on 2017/4/27.
 */

public interface ProperValueService extends BaseService<PropertiesValue, Integer> {

    String addPrpoValue(String jsonData) throws YesmywineException;

    String deletePrpoValue(String id)throws YesmywineException;

    PropertiesValue findById(Integer id);
}
