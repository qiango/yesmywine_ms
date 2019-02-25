package com.yesmywine.activity.ifttt.dao;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.activity.entity.Activity;
import com.yesmywine.base.record.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by wangdiandian on 2017/1/10.
 */
@Repository
public interface ActivityDao extends BaseRepository<Activity, Integer> {

    List<Activity> findByIdAndIsDeleteAndStatus(Integer activityId, DeleteEnum isDelete, ActivityStatus status);

    List<Activity> findByIsDeleteAndStatus(DeleteEnum isDelete, ActivityStatus status);

    @Query("select a from Activity a , IftttRegulation r where a.id=r.activityId and r.id=:regulationId")
    Activity findByRegulationId(@Param("regulationId") Integer regulationId);

    List<Activity> findByStatusNotAndStartTimeGreaterThanEqual(ActivityStatus activityStatus,Date endTime);

    List<Activity> findByStatusNotAndStartTimeGreaterThanEqualAndActionCode(ActivityStatus activityStatus,Date endTime,String actionCode);

    List<Activity> findByStatusNotAndEndTimeLessThanEqual(ActivityStatus activityStatus,Date startTime);

    List<Activity> findByStatusNotAndEndTimeLessThanEqualAndActionCode(ActivityStatus activityStatus,Date startTime,String actionCode);

    List<Activity> findByStatusNot(ActivityStatus overdue);

    List<Activity> findByIsDeleteAndStatusAndAuditStatus(DeleteEnum isDelete, ActivityStatus status, Integer auditStatus);

    List<Activity> findByIdInOrderByPriorityDesc(List<Integer> activityIdList);
}
