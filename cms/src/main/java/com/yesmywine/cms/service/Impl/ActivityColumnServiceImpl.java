package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.cms.dao.*;
import com.yesmywine.cms.entity.*;
import com.yesmywine.cms.service.ActivityColumnService;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class ActivityColumnServiceImpl implements ActivityColumnService {

    @Autowired
    private ActivityColumnDao activityColumnDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ActivitySecentDao activitySecentDao;
    @Autowired
    private GoodsService goodsService;

    @Override
    public Object findOne(Integer id) {
        ActivityColumn activityColumn = this.activityColumnDao.findOne(id);
        JSONObject jsonObjectColumn = new JSONObject();
        jsonObjectColumn.put("activityColumn", activityColumn);
        List<ActivitySecent> goodsList = this.activitySecentDao.findByColumnId(activityColumn.getId());
        JSONArray jsonArrayGoods = new JSONArray();
        for(ActivitySecent activitySecent:goodsList){
            JSONObject jsonObjectGoods = new JSONObject();
            jsonObjectGoods.put("id", activitySecent.getColumnId());
            Integer goodsId = activitySecent.getGoodsId();
            Goods goods = this.goodsService.findById(goodsId);
            jsonObjectGoods.put("goodsId", goods.getId());
            jsonObjectGoods.put("goodsName", goods.getGoodsName());
            jsonObjectGoods.put("image", goods.getPicture());
            jsonObjectGoods.put("salePrice", goods.getSalePrice());
            jsonObjectGoods.put("price", goods.getPrice());
            jsonObjectGoods.put("sales", goods.getSales());
            jsonArrayGoods.add(jsonObjectGoods);
        }
        jsonObjectColumn.put("goods", jsonArrayGoods);
        return jsonObjectColumn;
    }

    @Override
    public Object findAll() {
        List<ActivityColumn> activityColumns = this.activityColumnDao.findAll();
        JSONArray jsonArray = new JSONArray();
        for(ActivityColumn activityColumn: activityColumns) {
            JSONObject jsonObjectColumn = new JSONObject();
            jsonObjectColumn.put("activityColumn", activityColumn);
            List<ActivitySecent> goodsList = this.activitySecentDao.findByColumnId(activityColumn.getId());
            JSONArray jsonArrayGoods = new JSONArray();
            for (ActivitySecent activitySecent : goodsList) {
                JSONObject jsonObjectGoods = new JSONObject();
                jsonObjectGoods.put("id", activitySecent.getColumnId());
                Integer goodsId = activitySecent.getGoodsId();
                Goods goods = this.goodsService.findById(goodsId);
                jsonObjectGoods.put("goodsId", goods.getId());
                jsonObjectGoods.put("goodsName", goods.getGoodsName());
                jsonObjectGoods.put("image", goods.getPicture());
                jsonObjectGoods.put("salePrice", goods.getSalePrice());
                jsonObjectGoods.put("price", goods.getPrice());
                jsonObjectGoods.put("sales", goods.getSales());
                jsonArrayGoods.add(jsonObjectGoods);
            }
            jsonObjectColumn.put("goods", jsonArrayGoods);
            jsonArray.add(jsonObjectColumn);
        }
        return jsonArray;
    }
}
