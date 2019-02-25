package com.yesmywine.user.service;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.WineCellarFlow;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by ${shuang} on 2017/6/21.
 */
public interface WineCellarFlowService  extends BaseService<WineCellarFlow,Integer> {
    String page(PageModel pageModel) throws YesmywineException;
}
