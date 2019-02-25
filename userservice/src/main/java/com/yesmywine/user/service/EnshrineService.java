package com.yesmywine.user.service;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.Enshrine;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by ${shuang} on 2017/6/13.
 */
public interface EnshrineService extends BaseService<Enshrine,Integer> {
    Object enshrine(Integer userId, String goodsId,Integer status) throws YesmywineException;//收藏
    String page(PageModel pageModel)throws YesmywineException;//替换content
}
