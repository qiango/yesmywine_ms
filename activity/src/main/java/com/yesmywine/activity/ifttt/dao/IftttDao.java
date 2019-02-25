package com.yesmywine.activity.ifttt.dao;

import com.yesmywine.activity.ifttt.entity.IftttEntity;
import com.yesmywine.activity.ifttt.entity.IftttEnum;
import com.yesmywine.base.record.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Repository
public interface IftttDao extends BaseRepository<IftttEntity, Integer> {
    IftttEntity findById(Integer actionId);

    List<IftttEntity> findByType(IftttEnum type);

    IftttEntity findByCode(String triggerCode);
}
