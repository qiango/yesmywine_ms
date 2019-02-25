package com.yesmywine.evaluation.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.evaluation.bean.AdviceType;
import com.yesmywine.evaluation.dao.AdviceDao;
import com.yesmywine.evaluation.service.AdviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *咨询类型
 * Created by light on 2017/2/10.
 */
@Service
public class AdviceServiceImpl extends BaseServiceImpl<AdviceType, Integer> implements AdviceService {

    @Autowired
    private AdviceDao adviceRepository;

    //插入和修改
    public String saveAdviceType(AdviceType adviceType) {
        this.adviceRepository.save(adviceType);
        return "success";
    }

    //删除
    public String deleteById(Integer id) {
        this.adviceRepository.delete(id);
        return "success";
    }


}
