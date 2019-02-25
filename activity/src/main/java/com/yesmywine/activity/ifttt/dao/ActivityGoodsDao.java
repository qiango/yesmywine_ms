package com.yesmywine.activity.ifttt.dao;

import com.yesmywine.activity.entity.ActivityGoods;
import com.yesmywine.activity.entity.GoodsMirroring;
import com.yesmywine.base.record.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by SJQ on 2017/6/13.
 */
public interface ActivityGoodsDao extends BaseRepository<ActivityGoods, Integer> {
    List<ActivityGoods> findByActivityId(Integer activityId);

    void deleteByRegulationId(Integer id);
    @Query(value = "select ag.*  from activityGoods ag where goodsId=:goodsId " ,nativeQuery = true)
    List<ActivityGoods> findByGoods(@Param("goodsId") String goodsId);

    @Query("select count(distinct ag.goods)  from ActivityGoods ag where activityId=:activityId ")
    Integer findByActivityIdDiscountGoods(@Param("activityId") Integer activityId);

    void deleteByActivityId(Integer activityId);

    @Query(value = "select ag.*  from activityGoods ag left join activity a on a.id=ag.activityId where ag.goodsId=:targetId and a.actionCode='rushPurA' and a.endTime>TO_SECONDS(:startTime) " ,nativeQuery = true)
    List<ActivityGoods> isJoinOtherRushPur(@Param("targetId") Integer targetId,@Param("startTime")String startTime);

    @Query(value = "SELECT distinct ag.goodsId from activityGoods ag where ag.activityId in (:activityIds) and ag.goodsId in (:goodsIds)",nativeQuery = true)
    List<String> checkGoodsIsJoinOtherActivity(@Param("activityIds")List<Integer> activityIds,@Param("goodsIds")String[] goodsIds);

    @Query(value = "SELECT distinct ag.goodsId from activityGoods ag where ag.activityId in (:activityIds) and ag.goodsId in (:goodsIds)",nativeQuery = true)
    List<String> checkGoodsIsJoinShareActivity(@Param("activityIds")List<Integer> activityIds,@Param("goodsIds")String[] goodsIds);

    @Query(value = "SELECT ag.* from activityGoods ag left join activity a on ag.activityId=a.id where ag.goodsId=:goodsId and a.status=:status and a.actionCode<>'rushPurA'",nativeQuery = true)
    List<ActivityGoods> findByGoodsIdAndActivityStatus(@Param("goodsId") String goodsId,@Param("status") String status);

    ActivityGoods findByActivityIdAndGoodsId(Integer activityId, Integer goodsId);

    void deleteByActivityIdAndGoods(Integer activityId, GoodsMirroring goods);
}
