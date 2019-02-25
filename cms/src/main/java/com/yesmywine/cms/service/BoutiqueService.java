package com.yesmywine.cms.service;

import com.yesmywine.util.error.YesmywineException;

/**
 * Created by wangdiandian on 2017/6/1.
 */
public interface BoutiqueService {

    Object findOne(Integer boutiqueFirstId);

    Object findAll();

    Object FrontfindAll(Integer pageNo, Integer pageSize);

    String insert(String name, String jsonString);

    String update(Integer id, String name, String jsonString);

    String deleteFirst(Integer id);

    String deleteSecent(Integer id);

    com.alibaba.fastjson.JSON getShuffling()throws YesmywineException;
}
