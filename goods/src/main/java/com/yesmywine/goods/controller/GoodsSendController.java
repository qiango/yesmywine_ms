package com.yesmywine.goods.controller;

import org.springframework.web.bind.annotation.*;

/**
 * Created by hz on 2016/12/9.
 * 商品下发接口
 */
@RestController
@RequestMapping("/send")
public class GoodsSendController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String query(String goodsName, Integer skuId){
        return null;//TODO
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(){
        return null;//TODO
    }


}
