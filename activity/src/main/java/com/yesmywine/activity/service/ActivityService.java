package com.yesmywine.activity.service;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.activity.entity.Activity;
import com.yesmywine.activity.entity.ActivityType;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.cache.annotation.Cacheable;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/1/9.
 */

public interface ActivityService extends BaseService<Activity, Integer> {
    String createActivity(Map<String, String> param, HttpServletRequest request) throws YesmywineException;

    String deleteActivity(Integer activityId) throws YesmywineException;

    String updateActivity(Map<String, String> param, HttpServletRequest request) throws YesmywineException;

    String audit(Integer activityId, HttpServletRequest request) throws YesmywineException;

    List<Activity> findByIsDeleteAndStatus(DeleteEnum isDelete, ActivityStatus status);

    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'Activity_'+#id")
    Activity findById(Integer id) ;

    String startActivity(Activity activity);

    String endActivity(Activity activity);

    Activity findByRegulationId(Integer regulationId);

    String cancelActivity(Integer activityId) throws YesmywineException;

    List<ActivityType> findByType(String type);

    String reject(Integer activityId, HttpServletRequest request) throws YesmywineException;

    List<Activity> getStartTimeGtEndTime(Date endTime);

    List<Activity> getEndTimeLtStartTime(Date startTime);

    Object submitAudit(Integer activityId) throws YesmywineException;

    List<Activity> findByIsDeleteAndStatusAndAuditStatus(DeleteEnum isDelete, ActivityStatus status, Integer auditStatus);

    Object addGoods(Integer activityId, String goodsId) throws YesmywineException;

    Object delGoods(Integer id);

    PageModel getGoods(Integer activityId, String goodsName, Integer pageNo, Integer pageSize);

    List<Activity> findByIds(String activityIdList);

    List<Activity> findByIdList(List<Integer> activityIdList);
}
