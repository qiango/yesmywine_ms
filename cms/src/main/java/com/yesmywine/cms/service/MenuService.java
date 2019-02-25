package com.yesmywine.cms.service;

/**
 * Created by hz on 2017/5/16.
 */
public interface MenuService {

    Object findOne(Integer menuFirstId);

    Object findAll();

    String insert(Integer firstCategoryId, Integer firstIndex, String jsonString);

    String update(Integer id, Integer firstCategoryId, Integer firstIndex, String jsonString);

    String deleteFirst(Integer id);

    String deleteSecent(Integer id);
}
