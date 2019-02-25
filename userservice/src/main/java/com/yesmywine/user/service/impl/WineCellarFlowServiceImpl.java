package com.yesmywine.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.user.dao.PerDayPrizeDao;
import com.yesmywine.user.dao.WineCellarFlowDao;
import com.yesmywine.user.entity.WineCellarFlow;
import com.yesmywine.user.service.WineCellarFlowService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * Created by ${shuang} on 2017/6/21.
 */
@Service
public class WineCellarFlowServiceImpl extends BaseServiceImpl<WineCellarFlow,Integer> implements WineCellarFlowService {

        @Autowired
        private PerDayPrizeDao perDayPrizeDao;
        @Autowired
        private WineCellarFlowDao wineCellarFlowDao;
//        分页格式化
    @Override
    public String page(PageModel pageModel) throws YesmywineException {
//        请求拿到存酒单价
        Double univalent = perDayPrizeDao.findByPrizeId(1).getPrize();
//        获得分页数据
        List<WineCellarFlow> content = pageModel.getContent();
        List<JSONObject> newContent = new ArrayList<>();
        Set<String> sets = new HashSet<>();
        Integer userId = null;
        for (int i = 0; i <content.size() ; i++) {
            WineCellarFlow wineCellarFlow = content.get(i);
            String  orderNumber =wineCellarFlow.getOrderNumber();
            userId = wineCellarFlow.getUserId();//用户Id
            sets.add(orderNumber);
        }
        JSONObject jsonObject = new JSONObject();
        for (String orderNumber:sets) {
            List<WineCellarFlow> keepWines = wineCellarFlowDao.findByUserIdAndOrderNumber(Integer.valueOf(userId),orderNumber);
            JSONObject jsonObject1 =new JSONObject();
            JSONArray goodsArray = new JSONArray();
            Date createTime =null;
            Double sumPrize =0.0;
            for (  WineCellarFlow wineCellarFlow:keepWines) {
//                计算商品存酒价格
                JSONObject goodsObject =new JSONObject();
                Integer goodsId = wineCellarFlow.getGoodsId();//商品Id
                Integer extractable = wineCellarFlow.getCounts();//瓶数量
                Double perPrize = wineCellarFlow.getPerPrize();
                Integer freeDays = wineCellarFlow.getFreeDays();
                createTime = wineCellarFlow.getCreateTime();
                BigDecimal bigDecimal1 =new BigDecimal(extractable);
                BigDecimal bigDecimal2 =new BigDecimal(perPrize);
                Double BuyPrize = bigDecimal1.multiply(bigDecimal2).setScale(2, ROUND_HALF_DOWN).doubleValue();
                BigDecimal bigDecimal3 =new BigDecimal(BuyPrize);
                BigDecimal bigDecimal4 =new BigDecimal(sumPrize);
                sumPrize = bigDecimal4.add(bigDecimal3).setScale(2, ROUND_HALF_DOWN).doubleValue();
                String goodsName = wineCellarFlow.getGoodsName();
                goodsObject.put("goodsName",goodsName);
                goodsObject.put("goodsCount",extractable);
                goodsObject.put("univalent",univalent);
                goodsObject.put("freeDays",freeDays);
                goodsObject.put("BuyPrize",BuyPrize);
                goodsObject.put("goodsId",goodsId);
                goodsObject.put("status",1);
                goodsObject.put("goodsImageUrl",wineCellarFlow.getGoodsImageUrl());
                goodsArray.add(goodsObject);
            }
            jsonObject1.put("goods",goodsArray);
            jsonObject1.put("createTime",createTime);
            jsonObject1.put("sumPrize",sumPrize);
            jsonObject1.put("orderNumber",orderNumber);
            newContent.add(jsonObject1);
        }
        pageModel.setContent(newContent);
        pageModel.setTotalRows((long)newContent.size());
        return ValueUtil.toJson(pageModel);
    }
}
