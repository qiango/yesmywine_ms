package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.SaleService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 名庄特卖
 * Created by wangdiandian on 2017/5/26.
 */
@RestController
@RequestMapping("/cms/sale")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer id) {   //查看
        if(ValueUtil.notEmpity(id)){
            return ValueUtil.toJson(saleService.findOne(id));
        }
        return ValueUtil.toJson(saleService.findAll());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(String name,Integer positionId, String title, String jsonString) {   //新增
        try {
            ValueUtil.verify(name,"name");
            ValueUtil.verify(title,"title");
            ValueUtil.verify(positionId,"positionId");
            ValueUtil.verify(jsonString,"jsonString");
            return ValueUtil.toJson(HttpStatus.SC_CREATED, this.saleService.insert(name, positionId, title,jsonString));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, String name,Integer positionId, String title, String jsonString) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(title,"title");
            ValueUtil.verify(name,"name");
            return ValueUtil.toJson(HttpStatus.SC_CREATED, this.saleService.update(id, name, positionId, title, jsonString));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/first", method = RequestMethod.DELETE)
    public String deleteFirst(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.saleService.deleteFirst(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/secent", method = RequestMethod.DELETE)
    public String deleteSecent(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.saleService.deleteSecent(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/getShuffling", method = RequestMethod.GET)
    public String getShuffling() {
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, saleService.getShuffling());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

}
