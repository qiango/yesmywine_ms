package com.yesmywine.activity.service;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.ActivityGoods;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseService;

import java.util.List;

/**
 * Created by SJQ on 2017/6/16.
 */
public interface ActivityGoodsService extends BaseService<ActivityGoods, Integer> {
    PageModel findByActivityId(Integer activityId, Integer pageNo, Integer pageSize);

    void deleteByRegulationId(Integer id);

    List<ActivityGoods> findByGoodsId(String goodsId);

    void deleteByActivityId(Integer activityId);

    List<ActivityGoods> findByActivityId(Integer id);

    List<ActivityGoods> isJoinOtherRushPur(Integer targetId, String s);

    List<String> checkGoodsIsJoinOtherActivity(List<Integer> activityIds, String[] goodsIds);

    List<String> checkGoodsIsJoinShareActivity(List<Integer> activityIds, String[] goodsIds);

    List<ActivityGoods> findByGoodsIdAndActivityStatus(String goodsId, ActivityStatus current);

    void delete(Integer id);

    void deleteByActivityIdAndGoods(Integer activityId, Integer goodsId);
}
