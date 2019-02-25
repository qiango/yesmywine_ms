package com.yesmywine.cms.service;

/**
 * Created by hz on 2017/5/16.
 */
public interface PlateService {

    Object findOne(Integer plateFirstId);

    Object findByIsShow(Integer isShow);

    Object findAll();
    Object frontFindAll();

    String insert(Integer firstCategoryId, Integer firstIndex, String goodsJsonString,
                  Integer firstPositionId, Integer secentPositionId, Integer thirdPositionId, Integer fourthPositionId,
                  Integer appPositionId, String labelJsonString, String rankJsonString, Integer isShow);

    String update(Integer id, Integer firstCategoryId, Integer firstIndex, String goodsJsonString,
                  Integer firstPositionId, Integer secentPositionId, Integer thirdPositionId, Integer fourthPositionId,
                  Integer appPositionId, String labelJsonString, String rankJsonString, Integer isShow);

    String deleteFirst(Integer id);

    String deleteSecentGoods(Integer id);

    String deleteSecentLabel(Integer id);

    String deleteSecentRank(Integer id);
}
