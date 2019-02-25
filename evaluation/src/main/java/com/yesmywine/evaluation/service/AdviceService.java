package com.yesmywine.evaluation.service;


import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.evaluation.bean.AdviceType;

/**
 * Created by light on 2017/1/9.
 */
public interface AdviceService extends BaseService<AdviceType, Integer> {

    String saveAdviceType(AdviceType adviceType);

    String  deleteById(Integer id);
}
