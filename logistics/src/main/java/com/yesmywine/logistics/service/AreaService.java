package com.yesmywine.logistics.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.logistics.entity.Area;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;

/**
 * Created by wangdiandian on 2017/4/13.
 */
public interface AreaService extends BaseService<Area, Integer> {
    List<Area> showArea() throws YesmywineException;

}
