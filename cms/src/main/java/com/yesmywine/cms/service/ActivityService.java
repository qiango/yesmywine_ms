package com.yesmywine.cms.service;

/**
 * Created by hz on 2017/5/16.
 */
public interface ActivityService {

    Object findOne(Integer id);

    Object findOneMajor(Integer id);

    Object findAll();

    Object findAllApp();

    Object findTemplate(Integer id);

    Integer findTempl(Integer id);

    Object findByAppPosition(String appPosition);

    Object findAppByAppPosition(String appPosition);

    Object findByAppPositionMajor(String appPosition);

    String insert(String pageJsonString,String appJsonString, Integer id, String columnJsonString
            , Integer columnId, String goodsJsonString);

    String update(Integer id, String pageJsonString, String appJsonString, Integer columnId, String columnJsonString
            ,Integer activitySecentId, String goodsJsonString);

    String deleteFirst(Integer id);

    String deleteColumn(String ids);

    String deleteSecent(String ids);
}
