package com.yesmywine.cms.service;

/**
 * Created by hz on 2017/5/16.
 */
public interface LocationService {

    Object findAreaAll();

    Object findUserAreaOne(Integer userId);

    String insert(Integer userId, Integer areaId);

}
