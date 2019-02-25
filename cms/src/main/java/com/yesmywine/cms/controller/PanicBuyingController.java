package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.PanicBuyingService;
import com.yesmywine.util.basic.RestJson;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 超值抢购111
 * Created by wangdiandian on 2017/5/26.
 */
@RestController
@RequestMapping("/cms/panicBuying")
public class PanicBuyingController {

    @Autowired
    private PanicBuyingService panicBuyingService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer id) {   //查看
        if(ValueUtil.notEmpity(id)){
            return ValueUtil.toJson(panicBuyingService.findOne(id));
        }
        return ValueUtil.toJson(panicBuyingService.findAll());
    }


    @RequestMapping(value = "/goods", method = RequestMethod.GET)
    public Object goods(Integer pageNo, Integer pageSize) {   //查看
        Object all = panicBuyingService.findGoods(pageNo,pageSize);
        RestJson restJson = new RestJson();
        restJson.setCode("200");
        restJson.setMsg("success");
        restJson.setData(all);
        return restJson;
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(String name, Integer activityId, String jsonString) {   //新增
        try {
            ValueUtil.verify(name,"name");
            ValueUtil.verify(activityId,"activityId");
            ValueUtil.verify(jsonString,"jsonString");
            String insert = this.panicBuyingService.insert(name, activityId, jsonString);
            if("success".equals(insert)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }
            return ValueUtil.toError("500", insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, String name, Integer activityId, String jsonString) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(name,"name");
            ValueUtil.verify(activityId,"activityId");
            String update = this.panicBuyingService.update(id, name, activityId, jsonString);
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
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.panicBuyingService.deleteFirst(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/secent", method = RequestMethod.DELETE)
    public String deleteSecent(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.panicBuyingService.deleteSecent(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/getShuffling", method = RequestMethod.GET)
    public String getShuffling() {
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, panicBuyingService.getShuffling());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

}


