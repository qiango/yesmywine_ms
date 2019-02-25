package com.yesmywine.orders.controller;

import com.yesmywine.orders.service.FreightService;
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
 * Created by hz on 6/8/17.
 */
@RestController
@RequestMapping("/order/freight/itf")
public class FreightController {

    @Autowired
    private FreightService freightService;


    @RequestMapping(value = "/web",method = RequestMethod.GET)
    public String page( ) {   //查看
      return ValueUtil.toJson(HttpStatus.SC_OK,freightService.findAll()) ;
    }

    //插入
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam Map<String, String> params, String json) {
        // [{"id":1,"name":"北京市"},{"id":2,"name":"湖北省"}]
        try {
            ValueUtil.verify(params.get("areaName"));
            ValueUtil.verify(params.get("courierfees"));
            ValueUtil.verify(params.get("orderFree"));
            ValueUtil.verify(params.get("transfers"));
            ValueUtil.verify(params.get("trasfersOrder"));
            ValueUtil.verify(params.get("transfersAmount"));
            ValueUtil.verify(params.get("cashOnDelivery"));
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.freightService.saveAdviceType(params,json));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    //修改
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@RequestParam Map<String, String> params, String json) {
        try {
            ValueUtil.verify(params.get("id"));
            ValueUtil.verify(params.get("areaName"));
            ValueUtil.verify(params.get("courierfees"));
            ValueUtil.verify(params.get("orderFree"));
            ValueUtil.verify(params.get("transfers"));
            ValueUtil.verify(params.get("trasfersOrder"));
            ValueUtil.verify(params.get("transfersAmount"));
            ValueUtil.verify(params.get("cashOnDelivery"));
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.freightService.saveAdviceType(params,json));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    //删除
    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer id) {
        try {
            ValueUtil.verify(id, "id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,this.freightService.deleteById(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/freight",method = RequestMethod.GET)
    public String freight(String areaId,double orderAmount) {
        try {
            ValueUtil.verify(areaId, "areaId");
            ValueUtil.verify(orderAmount, "orderAmount");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,this.freightService.calculate(areaId,orderAmount));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

}
