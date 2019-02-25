package com.yesmywine.activity.service;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.activity.entity.IftttRegulation;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;
import java.util.Map;

/**
 * Created by SJQ on 2017/5/10.
 */
public interface RegulationService extends BaseService<IftttRegulation, Integer> {
    String create(Map<String, String> param) throws YesmywineException;

    String update(Map<String, String> param) throws YesmywineException;

    String delete(Integer id) throws YesmywineException;

    List<IftttRegulation> findByActivityIdAndIsDeleteAndStatus(Integer activityId, DeleteEnum isDelete, ActivityStatus status);

    List<IftttRegulation> findByTypeAndIsDeleteAndStatus(String goods, DeleteEnum isDelete, ActivityStatus status);

    List<IftttRegulation> findByActionIdAndIsDeleteAndStatus(Integer actionId, DeleteEnum isDelete, ActivityStatus status);

    IftttRegulation findById(Integer regulationId);

    List<IftttRegulation> findByActivityId(Integer activityId);

    Object getOne(Integer regulationId);

    List<IftttRegulation> findByActivityIdNoCache(Integer activityId);

    List<IftttRegulation> findByActivityIdAndIsDelete(Integer activityId, DeleteEnum notDelete);
}
