package com.yesmywine.cms.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hz on 2017/5/16.
 */
public interface RecommendService {

    Object findOne(Integer recommendFirstId);

    Object findAll();

    String insert(String name,Integer userId, String jsonString,String reasons);

    String update(Integer id, String name, Integer userId, String jsonString,String reasons);

    String deleteFirst(Integer id);

    String deleteSecent(Integer id);
}
