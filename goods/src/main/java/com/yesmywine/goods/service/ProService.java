
package com.yesmywine.goods.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entityProperties.Properties;
import com.yesmywine.util.error.YesmywineException;

import java.util.Map;


/**
 * Created by hz on 2/10/17.
 */

public interface ProService extends BaseService<Properties, Integer> {
    String addPrpo(Map<String, String> parm) throws YesmywineException; //新增属性

//    Map<String, List<String>> getProperByCategory(Integer categoryId) throws YesmywineException;

//    Object getMethods(Integer categoryId) throws YesmywineException;

//    Map<String, List<String>> getGeneralProp(Integer categoryId) throws YesmywineException;

    String updateProp(Integer id, String canSearch, String cnName, String isSku, String entryMode,String canShow) throws YesmywineException;

    String updateAdd(Integer propId, String code, String value) throws YesmywineException;

    String deleteProp(Map<String,String> map) throws YesmywineException;

    String synchronous(Map<String,String> map)throws YesmywineException;

    Properties findById(Integer id);
}

