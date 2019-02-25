//package com.hzbuvi.cms.controller;
//
//
//import GoodsService;
//import ValueUtil;
//import YesmywineException;
//import org.apache.http.HttpStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
///**
// * Created by hz on 12/8/16.
// */
//@RestController
//@RequestMapping("/cms/goods")
//public class GoodsController {
//    @Autowired
//    private GoodsService goodsService;
//
//    @RequestMapping(value = "/synchronous", method = RequestMethod.POST)
//    public String synchronous(String jsonDate) {   //同步商品
//        try {
//            return ValueUtil.toJson(HttpStatus.SC_OK,goodsService.synchronous(jsonDate));
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(HttpStatus.SC_NO_CONTENT,"Erro");
//        }
//    }
//
//}



