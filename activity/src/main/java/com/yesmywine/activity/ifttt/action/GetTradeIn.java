package com.yesmywine.activity.ifttt.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.activity.bean.WareEnum;
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
 * Created by SJQ on 2017/5/15.
 * 换购
 */
@Component
public class GetTradeIn implements Action {
    @Autowired
    private RegulationGoodsService regulationGoodsService;
    @Autowired
    private GoodsService goodsService;

    @Override
    public Map<String, Object> run(Map<String, Object> param) {
        String mainGoodsId = (String) param.get("goodsId");
        Integer regulationId = (Integer) param.get("regulationId");
        List<RegulationGoods> discountGoodsList = regulationGoodsService.findByRegulationIdAndWare(regulationId,WareEnum.TradeIn);
        List<String> goodsIdList = new ArrayList<>();
        String action = "";
        for (int i = 0; i < discountGoodsList.size(); i++) {
            RegulationGoods dg = discountGoodsList.get(i);
            String targetId = String.valueOf(dg.getTargetId());
            goodsIdList.add(targetId);
            if (i == discountGoodsList.size() - 1) {
                action += targetId;
            } else {
                action += targetId + ";";
            }
        }
        List<GoodsMirroring> goodsList = goodsService.findByGoodsIdIn(goodsIdList);
        JSONArray giftArr = new JSONArray(goodsList.size());
        for(GoodsMirroring goods:goodsList){
            goods.setGift(true);
            JSONObject giftObj = new JSONObject();
            giftObj.put("goodsId",goods.getGoodsId());
            giftObj.put("goodsImageUrl",goods.getGoodsImageUrl());
            giftObj.put("goodsName",goods.getGoodsName());
            giftObj.put("skuInfo",goods.getSkuInfo());
            giftArr.add(giftObj);
        }
        //            param.put("action", JSONUtil.objectToJsonStr(goodsList).replace("\"","'"));
        param.put("action", giftArr);
        param.put("tradeIn", param.get("actionValue"));
        return param;
    }
}
