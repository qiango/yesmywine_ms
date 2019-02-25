package com.yesmywine.activity.ifttt.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.activity.ifttt.Action;
import com.yesmywine.activity.entity.RegulationGoods;
import com.yesmywine.activity.service.RegulationGoodsService;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SJQ on 2017/5/11.
 * 赠优惠券
 */
@Component
public class GetCoupons implements Action {
    @Autowired
    private RegulationGoodsService regulationGoodsService;

    @Override
    public Map<String, Object> run(Map<String, Object> param) {
        String mainGoodsId = (String) param.get("goodsId");
        Integer regulationId = (Integer) param.get("regulationId");
        List<RegulationGoods> regulationGoodsList = regulationGoodsService.findByRegulationIdAndWare(regulationId,  WareEnum.Coupon);
        String action = "";
        for (int i = 0; i < regulationGoodsList.size(); i++) {
            RegulationGoods dg = regulationGoodsList.get(i);
            String targetId = String.valueOf(dg.getTargetId());
            if (i == regulationGoodsList.size() - 1) {
                action += targetId;
            } else {
                action += targetId + ";";
            }
        }
        //获取可用的优惠券
        Map<String,Object> params = new HashMap<>();
        params.put("couponIds",action.replace(";",","));
        if(param.get("username")!=null){
            params.put("username",param.get("username"));
        }
//        params.put("username","234");
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST,"/userservice/coupon/filter/itf", RequestMethod.get,params,null);
        if(result!=null&&!result.equals("")){
            String code = JSON.parseObject(result).getString("code");
            String data = JSON.parseObject(result).getString("data");
            JSONArray coupons = JSON.parseArray(data);
            JSONArray shortCoupons = new JSONArray();
            for(int i=0;i<coupons.size();i++){
                JSONObject couponObj = (JSONObject) coupons.get(i);
                JSONObject shortCouponObj = new JSONObject();
                shortCouponObj.put("full",couponObj.getString("full"));
                shortCouponObj.put("cut",couponObj.getString("cut"));
                shortCouponObj.put("activeStartTime",couponObj.getString("activeStartTime"));
                shortCouponObj.put("activeEndTime",couponObj.getString("activeEndTime"));
                shortCouponObj.put("id",couponObj.getInteger("id"));
                shortCoupons.add(shortCouponObj);
            }
            if(code!=null){
                action = shortCoupons.toJSONString();
            }else{
                action = "";
                System.out.println("用户服务未返回优惠券数据");
            }
        }else{
            action = "";
            System.out.println("用户服务未返回优惠券数据");
        }

        param.put("action", action);
        return param;
    }
}
