package com.yesmywine.activity.service;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;
import java.util.Map;

/**
 * Created by SJQ on 2017/5/10.
 */
public interface UseService {
    JSONObject runCart(String jsonData, String username) throws YesmywineException;

    List<Map<String, Object>> runOrder(Double totalPrice) throws YesmywineException;

    Object runSingle(Map<String, String> params) throws YesmywineException;

    PageModel getActivityGoods(Integer activityId, Integer pageNo, Integer pageSize);

    Object getActivityInfo(Integer activityId, Integer pageNo, Integer pageSize);
}
