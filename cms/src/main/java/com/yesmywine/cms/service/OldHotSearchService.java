package com.yesmywine.cms.service;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldHotSearchService {

    Object findOne(Integer id);

    Object findAll();

    String insert(String name, String jsonString);

    String update(Integer id, String name, String jsonString);

    String deleteFirst(Integer id);

    String deleteSecent(Integer id);
}
