package com.yesmywine.goods.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.dao.ProperValueDao;
import com.yesmywine.goods.entityProperties.PropertiesValue;
import com.yesmywine.goods.service.ProperValueService;
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
 * Created by wangdiandian on 2017/4/27.
 */
@RestController
@RequestMapping("/goods/properValue")
public class PropValueController {
    @Autowired
    private ProperValueService properValueService;
    @Autowired
    private ProperValueDao properValueDao;


    @RequestMapping(method = RequestMethod.GET)
    public String page(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize) {   //查看
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            return ValueUtil.toJson(properValueService.findAll());
        } else if (null != params.get("all")) {
            params.remove(params.remove("all").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        return ValueUtil.toJson(HttpStatus.SC_OK,properValueService.findAll(pageModel));
    }

    @RequestMapping(value = "/showOne",method = RequestMethod.GET)
    public String getOne(Integer valueId){
        PropertiesValue propertiesValue = properValueDao.findOne(valueId);
        return ValueUtil.toJson(propertiesValue.getCnValue());
    }
    @RequestMapping(value = "/showOne/itf",method = RequestMethod.GET)
    public String getNewOne(Integer valueId){
        PropertiesValue propertiesValue = properValueDao.findOne(valueId);
        return ValueUtil.toJson(propertiesValue.getCnValue());
    }

    @RequestMapping(value = "/syncreate",method = RequestMethod.POST)
    public String create(String jsonData) {//pass新增到同步到商城
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, properValueService.addPrpoValue(jsonData));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/syndelete",method = RequestMethod.POST)
    public String deleteProperValue(String id) {//pass删除到同步到商城
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, properValueService.deletePrpoValue(id));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

}
