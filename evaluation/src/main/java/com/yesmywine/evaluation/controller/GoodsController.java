package com.yesmywine.evaluation.controller;


import com.yesmywine.evaluation.service.GoodsService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hz on 12/8/16.
 */
@RestController
@RequestMapping("/evaluation/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/synchronous", method = RequestMethod.POST)
    public String synchronous(String jsonData) {   //同步商品
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,goodsService.synchronous(jsonData));
        } catch (YesmywineException e) {
            return ValueUtil.toError(HttpStatus.SC_NO_CONTENT,"Erro");
        }
    }

}



