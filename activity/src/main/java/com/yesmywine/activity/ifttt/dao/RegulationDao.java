package com.yesmywine.activity.ifttt.dao;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.activity.entity.IftttRegulation;
import com.yesmywine.base.record.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface RegulationDao extends BaseRepository<IftttRegulation, Integer> {

    List<IftttRegulation> findByActivityIdAndIsDelete(Integer activityId, DeleteEnum isDelete);

    IftttRegulation findById(Integer discountId, String goods);

    IftttRegulation findByIdAndIsDelete(Integer discountId, String goods, DeleteEnum isDelete, ActivityStatus status);

    List<IftttRegulation> findByAndIsDelete(String goods, DeleteEnum isDelete, ActivityStatus status);

    List<IftttRegulation> findByActionIdAndIsDelete(Integer actionId, DeleteEnum isDelete);

    List<IftttRegulation> findByIsDelete(DeleteEnum isDelete, ActivityStatus status);

    IftttRegulation findById(Integer regulationId);

    List<IftttRegulation> findByActivityId(Integer activityId);

    List<IftttRegulation> findByActivityIdIn(List<Integer> activityIdList);

    List<IftttRegulation> findByActivityIdOrderByPriorityDesc(Integer activityId);
}
