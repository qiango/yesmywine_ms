package com.yesmywine.activity.thread;

import com.yesmywine.activity.entity.Activity;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.date.DateUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by by on 2017/7/10.
 */
public class ChangeGoosInfoThread implements Runnable {
    private String goodsIds;
    private String price;
    private String counts;
    private String saleModel;
    private Activity activity;

    public ChangeGoosInfoThread(String goodsIds, String price, String counts,String saleModel,Activity activity) {
        this.goodsIds = goodsIds;
        this.price = price;
        this.counts = counts;
        this.saleModel = saleModel;
        this.activity = activity;
    }

    @Override
    public void run() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("goodsId", goodsIds);
        paramMap.put("price", price);
        paramMap.put("count", counts);
        paramMap.put("saleModel", saleModel);
        paramMap.put("startTime", DateUtil.toString(activity.getStartTime(),"yyyy-MM-dd HH:mm:ss"));
        paramMap.put("endTime", DateUtil.toString(activity.getEndTime(),"yyyy-MM-dd HH:mm:ss"));
        String result = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/goods/goods/updateBookGoods/itf", RequestMethod.post, paramMap, null);
        System.out.println("===========================调用商品抢购设置接口："+result);
    }
}
