package com.yesmywine.user.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.user.dao.LevelHistoryDao;
import com.yesmywine.user.entity.LevelHistory;
import com.yesmywine.user.service.HistoryService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ${shuang} on 2017/4/12.
 */
@Service
public class HistoryServiceImpl extends BaseServiceImpl<LevelHistory, Integer> implements HistoryService {

    @Autowired
    private LevelHistoryDao levelHistoryDao;
    @Override
    public String delete(String ids) throws YesmywineException {
        //删除升级记录，支持批量删除
        ValueUtil.verify(ids);
        String [] idLIst = ids.split(",");
        for (int i = 0; i <idLIst.length ; i++) {
            levelHistoryDao.delete(Integer.parseInt(idLIst[i]));
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }
}
