package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.LevelHistory;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by ${shuang} on 2017/4/12.
 */
public interface HistoryService extends BaseService<LevelHistory,Integer> {
        String delete(String ids) throws YesmywineException;

}
