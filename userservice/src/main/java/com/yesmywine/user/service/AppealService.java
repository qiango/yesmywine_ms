package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.Appeal;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by ${shuang} on 2017/4/6.
 */
public interface AppealService extends BaseService<Appeal,Integer> {
    //客户申诉
    String  appeal(Integer userId,String justification ) throws YesmywineException;
    //管理员处理申诉
    String  feedback(Integer userId,String feedback,Integer blackStatus) throws YesmywineException;

}
