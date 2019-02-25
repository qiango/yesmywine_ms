package com.yesmywine.goods.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entity.Label;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;
import java.util.Map;

/**
 * Created by hz on 2017/4/24.
 */
public interface LabelService extends BaseService<Label, Integer> {

//    Map<String, String> query(String name);

    String create(Map<String, String> map)throws YesmywineException;

    String put(Map<String, Object> map)throws YesmywineException;

    String deleteLabel(Integer id) throws YesmywineException ;

    String deleteGoods(Integer id) throws YesmywineException;

//    List<Object> findIndex();
//
//    Object findByName(String name);
//    Object findlist();
}
