package com.yesmywine.evaluation.service.impl;

import com.yesmywine.evaluation.bean.Lable;
import com.yesmywine.evaluation.dao.LableDao;
import com.yesmywine.evaluation.service.LableService;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hz on 6/21/17.
 */
@Service
public class LableServiceImpl implements LableService {
    @Autowired
    private LableDao lableDao;

    //新增修改
    @Override
    public String save(Lable lable) throws YesmywineException {
        if(null==lable.getId()){
            lableDao.save(lable);
        }else {
            Lable lable1=lableDao.findOne(lable.getId());
            lable1.setLableName(lable.getLableName());
            lableDao.save(lable1);
        }
        return "success";
    }

    @Override
    public String delete(Integer id) throws YesmywineException {
        lableDao.delete(id);
        return "success";
    }
}
