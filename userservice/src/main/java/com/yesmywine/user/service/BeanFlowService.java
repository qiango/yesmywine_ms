package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.BeanFlow;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by ${shuang} on 2017/4/12.
 */
public interface BeanFlowService  extends BaseService<BeanFlow,Integer> {


    String consume(Integer userId, Integer bean,String oderNumber,String channelCode) throws YesmywineException;

    String beanFlowSys(String jsonData);

    String syntopass(Integer beanuserId);
}
