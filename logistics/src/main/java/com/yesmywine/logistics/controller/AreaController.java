package com.yesmywine.logistics.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.logistics.dao.AreaDao;
import com.yesmywine.logistics.entity.Area;
import com.yesmywine.logistics.service.AreaService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by wangdiandian on 2017/4/13.
 */
@RestController
@RequestMapping("web/logistics/area")
public class AreaController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private AreaDao areaDao;

    @RequestMapping(value = "secondLevel",method = RequestMethod.GET)
    public String showSecondLevel() throws YesmywineException {//查看城市二级
        try {
            System.out.println("开始查询    "+new Date());
            List<Area> list=areaService.showArea();
            System.out.println("查询完成    "+new Date());
            System.out.println("开始组合数据    "+new Date());

            com.alibaba.fastjson.JSONArray jsonArray2 = new com.alibaba.fastjson.JSONArray();
            for(int i=0;i<list.size();i++){
                Area area = list.get(i);
                List<Area> list1=areaDao.findByParentId(area.getAreaNo());
                com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                com.alibaba.fastjson.JSONArray jsonArray3 = new com.alibaba.fastjson.JSONArray();
                for(int j=0;j<list1.size();j++){
                    Area areaCate = list1.get(j);
                    com.alibaba.fastjson.JSONObject jsonObject2 = new com.alibaba.fastjson.JSONObject();
                    jsonObject2.put("value",areaCate.getAreaNo());
                    jsonObject2.put("label",areaCate.getCityName());
                    jsonArray3.add(jsonObject2);
                }
                jsonObject.put("value",list.get(i).getAreaNo()) ;
                jsonObject.put("label",list.get(i).getCityName());
                if(jsonArray3.size()>0){
                    jsonObject.put("children",jsonArray3);
                }
                jsonArray2.add(jsonObject);
            }
            System.out.println("组合数据完成    "+new Date());
            return ValueUtil.toJson(HttpStatus.SC_OK,jsonArray2);
        } catch (YesmywineException e) {
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Erro");
        }

    }

    @RequestMapping( method = RequestMethod.GET)
    public String showCatogory() throws YesmywineException {//查看城市三级
        try {
            System.out.println("开始查询    "+new Date());
            List<Area> list=areaService.showArea();
            System.out.println("查询完成    "+new Date());
            System.out.println("开始组合数据    "+new Date());

            com.alibaba.fastjson.JSONArray jsonArray2 = new com.alibaba.fastjson.JSONArray();
            for(int i=0;i<list.size();i++){
                Area area = list.get(i);
                List<Area> list1=areaDao.findByParentId(area.getAreaNo());
                com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                com.alibaba.fastjson.JSONArray jsonArray3 = new com.alibaba.fastjson.JSONArray();
                for(int j=0;j<list1.size();j++){
                    Area areaCate = list1.get(j);
//                    Area area2 = list1.get(i);
                    List<Area> list2=areaDao.findByParentId(areaCate.getAreaNo());
                    com.alibaba.fastjson.JSONArray jsonArray4 = new com.alibaba.fastjson.JSONArray();
                    for(int k=0;k<list2.size();k++) {
                        Area areaCate1 = list2.get(k);
                        com.alibaba.fastjson.JSONObject jsonObject3 = new com.alibaba.fastjson.JSONObject();
                        jsonObject3.put("value",areaCate1.getId());
                        jsonObject3.put("label",areaCate1.getCityName());
                        jsonArray4.add(jsonObject3);
                    }
                    com.alibaba.fastjson.JSONObject jsonObject2 = new com.alibaba.fastjson.JSONObject();
                    jsonObject2.put("value",areaCate.getId());
                    jsonObject2.put("label",areaCate.getCityName());
                    jsonArray3.add(jsonObject2);
                    if(jsonArray4.size()>0){
                        jsonObject2.put("children",jsonArray4);
                    }
                }
                jsonObject.put("value",list.get(i).getId()) ;
                jsonObject.put("label",list.get(i).getCityName());
                if(jsonArray3.size()>0){
                    jsonObject.put("children",jsonArray3);
                }
                jsonArray2.add(jsonObject);
            }
            System.out.println("组合数据完成    "+new Date());
            return ValueUtil.toJson(HttpStatus.SC_OK,jsonArray2);
        } catch (YesmywineException e) {
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Erro");
        }
    }


}
