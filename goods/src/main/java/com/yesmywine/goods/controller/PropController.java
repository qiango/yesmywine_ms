
package com.yesmywine.goods.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.dao.ProperValueDao;
import com.yesmywine.goods.entityProperties.PropertiesValue;
import com.yesmywine.goods.service.ProService;
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

import java.util.List;
import java.util.Map;

/**
 * Created by hz on 1/6/17.
 */
@RestController
@RequestMapping("/goods/properties")
public class PropController {
    @Autowired
    private ProService propService;
    @Autowired
    private ProperValueDao properValueDao;
    //缺一个参数all
    // TODO: 2017/6/5  showFields不可用
    @RequestMapping(method = RequestMethod.GET)
    public String page(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize) {   //查看
        MapUtil.cleanNull(params);
        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            return ValueUtil.toJson(propService.findAll());
        } else if (null != params.get("all")) {
            params.remove(params.remove("all").toString());
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }

        if(ValueUtil.notEmpity(params.get("propertiesId"))){
            return ValueUtil.toJson(HttpStatus.SC_OK,propService.findById(Integer.valueOf(params.get("propertiesId").toString())));
        }

        if(ValueUtil.isEmpity(params.get("cnName_l"))){
            params.remove("cnName_l");
        }
        if(ValueUtil.isEmpity(params.get("code_l"))){
            params.remove("code_l");
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        return ValueUtil.toJson(HttpStatus.SC_OK,propService.findAll(pageModel));
    }

    @RequestMapping(value = "/showPropValue", method = RequestMethod.GET)
    public String showPropValue(String propId) { //通过属性id组找到该属性下的值
        String [] prop=propId.split(",");
        com.alibaba.fastjson.JSONArray jsonArray2 = new com.alibaba.fastjson.JSONArray();
        for(int i=0;i<prop.length;i++){
            List<PropertiesValue> p=properValueDao.findByPropertiesId(Integer.parseInt(prop[i]));
            com.alibaba.fastjson.JSONArray jsonArray1 = new com.alibaba.fastjson.JSONArray();
            for(int j=0;j<p.size();j++){
                com.alibaba.fastjson.JSONObject jsonObject1 = new com.alibaba.fastjson.JSONObject();
                jsonObject1.put("value",p.get(j).getId());
                jsonObject1.put("label",p.get(j).getCnValue());
                jsonObject1.put("code",p.get(j).getCode());
                jsonArray1.add(jsonObject1);
            }
            com.alibaba.fastjson.JSONObject jsonObject2 = new com.alibaba.fastjson.JSONObject();
            jsonObject2.put("id",prop[i]);
            if(jsonArray1.size()>0){
                jsonObject2.put("prop",jsonArray1);
            }
            jsonArray2.add(jsonObject2);
        }
        return ValueUtil.toJson(HttpStatus.SC_OK,jsonArray2);
    }

    @RequestMapping(value = "/synchronous", method = RequestMethod.POST)
    public String synchronous(@RequestParam Map<String,String> map){
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,propService.synchronous(map));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(HttpStatus.SC_NO_CONTENT,"Erro");
        }
    }

}