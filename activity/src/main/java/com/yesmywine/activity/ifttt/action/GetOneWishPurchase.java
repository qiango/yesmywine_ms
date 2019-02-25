package com.yesmywine.activity.ifttt.action;

import com.yesmywine.activity.ifttt.Action;
import com.yesmywine.activity.entity.GoodsMirroring;
import com.yesmywine.activity.entity.RegulationGoods;
import com.yesmywine.activity.ifttt.service.GoodsService;
import com.yesmywine.activity.service.RegulationGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by SJQ on 2017/5/10.
 * 随心购
 */
@Component
public class GetOneWishPurchase implements Action {

    @Autowired
    private RegulationGoodsService regulationGoodsService;
    @Autowired
    private GoodsService goodsService;

    @Override
    public Map<String, Object> run(Map<String, Object> param) {
        Integer regulationId = (Integer) param.get("regulationId");
        //找出有哪些随心购活动
        List<RegulationGoods> regulationGoodsList = regulationGoodsService.findByRegulationId(regulationId);
        List<String> goodsIdList = new ArrayList<>();
        for (int i = 0; i < regulationGoodsList.size(); i++) {
            RegulationGoods dg = regulationGoodsList.get(i);
            String targetId = String.valueOf(dg.getTargetId());
            goodsIdList.add(targetId);
        }
        List<GoodsMirroring> goodsList = goodsService.findAll(goodsIdList);
        //            param.put("action", JSONUtil.objectToJsonStr(goodsList).replace("\"","'"));
        param.put("action", goodsList);
        return param;
    }
}
