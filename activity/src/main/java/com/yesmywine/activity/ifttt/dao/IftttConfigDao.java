package com.yesmywine.activity.ifttt.dao;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.activity.ifttt.entity.IftttConfig;
import com.yesmywine.base.record.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface IftttConfigDao extends BaseRepository<IftttConfig, Integer> {
    List<IftttConfig> findByDiscountId(Integer discountId);

    IftttConfig findByDiscountIdAndIsDelete(Integer discountId, DeleteEnum isDelete);

    IftttConfig findByDiscountIdAndIsDeleteAndStatus(Integer discountId, DeleteEnum isDelete, ActivityStatus status);

    void deleteByDiscountId(Integer id);

}
