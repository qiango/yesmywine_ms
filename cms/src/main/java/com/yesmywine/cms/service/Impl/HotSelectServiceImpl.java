package com.yesmywine.cms.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.cms.dao.HotSelectDao;
import com.yesmywine.cms.entity.HotSelect;
import com.yesmywine.cms.service.HotSelectService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class HotSelectServiceImpl extends BaseServiceImpl<HotSelect, Integer> implements HotSelectService {

    @Autowired
    private HotSelectDao hotSelectDao;

    @Override
    public String insert(String name) {
        HotSelect byName = this.hotSelectDao.findByName(name);
        if(ValueUtil.notEmpity(byName)){
            return "热词已存在";
        }
        HotSelect hotSelect = new HotSelect();
        hotSelect.setName(name);
        try {
            this.hotSelectDao.save(hotSelect);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String delete(Integer id) {
        try{
            this.hotSelectDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }
}
