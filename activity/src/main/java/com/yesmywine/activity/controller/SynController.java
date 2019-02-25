package com.yesmywine.activity.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.activity.entity.GoodsMirroring;
import com.yesmywine.activity.service.SynService;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by SJQ on 2017/6/9.
 * 同步接收商品服务提供的商品信息
 */
@RestController
@RequestMapping("/activity/syn")
public class SynController {

    @Autowired
    private SynService synService;

    @RequestMapping(value = "/receiveGoodsInfo",method = RequestMethod.POST)
    public String receiveGoodsInfo(String jsonData){
        JSONObject goodsJson = JSON.parseObject(jsonData);
        String goodsId = goodsJson.getString("id");
        GoodsMirroring result = synService.saveGoodsInfo(jsonData, goodsId);
        return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
    }

}
