package com.yesmywine.user.service;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.StoreWineFlow;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by ${shuang} on 2017/8/14.
 */
public interface StoreWineFlowService extends BaseService<StoreWineFlow, Integer> {

    PageModel page(PageModel pageModel) throws YesmywineException;

}
