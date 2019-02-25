package com.yesmywine.cms.service.Impl;

import com.yesmywine.cms.dao.AppAdvertisingDao;
import com.yesmywine.cms.entity.AppAdvertising;
import com.yesmywine.cms.service.AppAdverService;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hz on 7/5/17.
 */
@Service
public class AppAdverServiceImpl implements AppAdverService {
    @Autowired
    private AppAdvertisingDao appAdvertisingDao;
    @Override
    public String save(AppAdvertising appAdvertising) throws YesmywineException {
        appAdvertisingDao.save(appAdvertising);
        return "success";
    }

    @Override
    public String delete(Integer id) throws YesmywineException {
        appAdvertisingDao.delete(id);
        return "success";
    }
}
