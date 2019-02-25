package com.yesmywine.cms.service;

/**
 * Created by hz on 2017/5/16.
 */
public interface LikeService {

    Object findOne(Integer likeFirstId);

    Object findAll();

    Object FrontfindAll(Integer pageNo, Integer pageSize);

    String insert(String name, String jsonString);

    String update(Integer id, String name, String jsonString);

    String deleteFirst(Integer id);

    String deleteSecent(Integer id);
}
