package com.yesmywine.goods.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.service.GiftCardHistoryService;
import com.yesmywine.goods.service.GiftCardService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by wangdiandian on 2017/4/21.
 */
@RestController
public class GiftCardController {
    @Autowired
    private GiftCardService giftCardService;
    @Autowired
    private GiftCardHistoryService giftCardHistoryService;


    @RequestMapping(value = "/goods/giftCard/createGiftCard",method = RequestMethod.POST)
    public String creatGiftCard(@RequestParam Map<String,String> param) {//商城新增礼品卡
            try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.addGiftCard(param));
        } catch (YesmywineException e) {
                Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/goods/giftCard/itf",method = RequestMethod.GET)//内部调用
    public String indexitf(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Long cardNumber) throws  Exception{
        MapUtil.cleanNull(params);
        if(cardNumber!=null){//查看礼品卡详情
            return ValueUtil.toJson(HttpStatus.SC_OK, giftCardService.loadCardNumber(cardNumber));
        }
        //查看礼品卡列表
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = giftCardService.findAll(pageModel);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }
    @RequestMapping(value="/goods/giftCard/index" ,method = RequestMethod.GET)//后台查看礼品卡
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Long id) throws  Exception{
        MapUtil.cleanNull(params);
        if(id!=null){//查看礼品卡详情
            return ValueUtil.toJson(HttpStatus.SC_OK, giftCardService.updateLoad(id));
        }
        //查看礼品卡列表
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = giftCardService.findAll(pageModel);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }

    @RequestMapping(value = "/goods/giftCard/spend/itf",method = RequestMethod.POST)
    public String spendGiftPass(String jsonData) {//商城礼品卡消费同步到pass接口
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.spendPassCard(jsonData));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/goods/giftCard/bound/itf",method = RequestMethod.PUT)
    public String bound(String jsonData) {//商城礼品卡绑定信息同步到pass
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.bound(jsonData));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

    }

    @RequestMapping(value = "/goods/giftCard/return/itf",method = RequestMethod.POST)
    public String returnGiftPass(String jsonData) {//订单退礼品卡金额
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.returnPassCard(jsonData));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

//    @RequestMapping(value = "/goods/giftCard/buyGiftCard/itf",method = RequestMethod.PUT)
//    public String buyGiftCard(Long cardNumber) {//商城购买礼品卡
//        try {
//            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.buyGiftCard(cardNumber));
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(),e.getMessage());
//        }
//    }
    @RequestMapping(value = "/goods/giftCard/randomGiftCard/itf",method = RequestMethod.POST)
    public String randomGiftCard(Integer skuId,Integer counts) {//随机抽取未购买的礼品卡
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.randomGiftCard(skuId,counts));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/goods/giftCard/ordersGift/itf",method = RequestMethod.POST)
    public String ordersGift(String cardNumbers) {//购买礼品卡后显示信息
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.ordersGift(cardNumbers));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    //以下是pass调用接口
    @RequestMapping(value = "/goods/giftCard/giftCard/itf",method = RequestMethod.POST)
    public String synchronizeGiftCard(String jsonData) {//礼品卡同步
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.synchronizeGiftCard(jsonData));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/goods/giftCard/boundGiftCard/ift",method = RequestMethod.PUT)
    public String synchronizeBound(String jsonData) {//pass礼品卡绑定同步到商城接口
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.boundGiftCard(jsonData));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/goods/giftCard/spendGiftCard/itf",method = RequestMethod.PUT)
    public String synchronizeSpend(String jsonData) {//pass礼品卡消费后同步到商城接口
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.spendGiftCard(jsonData));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/goods/giftCard/synchronizeHistory/itf",method = RequestMethod.POST)
    public String synchronizeCardHistory(String jsonData) {//pass礼品卡消费后同步到商城接口
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.giftCardHistory(jsonData));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value="/goods/giftCard/history/index" ,method= RequestMethod.GET)
    public String indexHistory(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize) throws  Exception{
        MapUtil.cleanNull(params);
        ValueUtil.verify(params.get("id"), "idNull");
        String giftCardId=params.get("id").toString();
//        params.put("deleteEnum", 0);
        params.remove(params.remove("id").toString());
        params.put("giftCardId",giftCardId);
        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
            return ValueUtil.toJson(giftCardHistoryService.findAll());
        }else if(null!=params.get("all")){
            params.remove(params.remove("all").toString());
        }
        //查看礼品卡消费列表
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = giftCardHistoryService.findAll(pageModel);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }

}
