package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.FlashPurchaseService;
import com.yesmywine.util.basic.RestJson;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 闪购111
 * Created by wangdiandian on 2017/5/26.
 */
@RestController
@RequestMapping("/cms/flashPurchase")
public class FlashPurchaseController {
    @Autowired
    private FlashPurchaseService flashPurchaseService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer id) {   //查看
        if(ValueUtil.notEmpity(id)){
            return ValueUtil.toJson(flashPurchaseService.findOne(id));
        }
        return ValueUtil.toJson(flashPurchaseService.findAll());
    }


    @RequestMapping(value = "/goods", method = RequestMethod.GET)
    public Object goods(Integer pageNo, Integer pageSize) {   //查看
        Object all = flashPurchaseService.findGoods(pageNo,pageSize);
        RestJson restJson = new RestJson();
        restJson.setCode("200");
        restJson.setMsg("success");
        restJson.setData(all);
        return restJson;
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(String name,Integer activityId, String jsonString) {   //新增
        try {
            ValueUtil.verify(name,"name");
            ValueUtil.verify(jsonString,"jsonString");
            ValueUtil.verify(activityId,"activityId");
            String insert = this.flashPurchaseService.insert(name, activityId, jsonString);
            if("success".equals(insert)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }
            return ValueUtil.toError("500", insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, String name,Integer activityId, String jsonString) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(name,"name");
            ValueUtil.verify(activityId,"activityId");
            ValueUtil.verify(jsonString,"jsonString");
            String update = this.flashPurchaseService.update(id, name, activityId, jsonString);
            if("success".equals(update)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
            }
            return ValueUtil.toError("500", update);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/first", method = RequestMethod.DELETE)
    public String deleteFirst(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.flashPurchaseService.deleteFirst(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/secent", method = RequestMethod.DELETE)
    public String deleteSecent(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.flashPurchaseService.deleteSecent(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

//    @RequestMapping(value = "/getShuffling", method = RequestMethod.GET)
//    public String getShuffling() {
//        try {
//            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, flashPurchaseService.getShuffling());
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(),e.getMessage());
//        }
//    }

}

