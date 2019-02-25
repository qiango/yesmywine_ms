//package com.hzbuvi.orders.controller;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import HttpBean;
//import ConstantData;
//import ValueUtil;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * Created by WANG, RUIQING on 12/26/16
// * Twitter : @taylorwang789
// * E-mail : i@wrqzn.com
// */
//@RestController
//@RequestMapping("/dic")
//public class DicController {
//
//    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
//    public String refresh() {
//        HttpBean bean = new HttpBean(ConstantData.urlDic + "/param", RequestMethod.get);
//        bean.run();
//        String response = bean.getResponseContent();
//        String data = ValueUtil.getFromJson(response, "data");
//        JsonParser jsonParser = new JsonParser();
//        JsonObject origin = jsonParser.parse(data).getAsJsonObject();
////	public static String urlUser= null ;
////	public static String urlActivity= null ;
////	public static String urlWarehouse= null ;
//        if (null != origin.get("url-user")) {
//            ConstantData.urlUser = origin.get("url-user").getAsString();
//        }
//        if (null != origin.get("url-sso")) {
//            ConstantData.urlSso = origin.get("url-sso").getAsString();
//        }
//        if (null != origin.get("url-warehouse")) {
//            ConstantData.urlWarehouse = origin.get("url-warehouse").getAsString();
//        }
//        return response;
//    }
//
//
//}
