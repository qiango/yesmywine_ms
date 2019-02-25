package com.yesmywine.activity.ifttt.action;

import com.yesmywine.activity.ifttt.Action;
import com.yesmywine.activity.ifttt.dao.RegulationGoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by SJQ on 2017/5/24.
 * 抢购
 */
@Component
public class RushPurchase implements Action {
    @Autowired
    private RegulationGoodsDao discountGoodsDao;

    @Override
    public Map<String, Object> run(Map<String, Object> param) {
        param.put("action", String.valueOf(param.get("goodsTotalPrice")));
        return param;
    }
}
