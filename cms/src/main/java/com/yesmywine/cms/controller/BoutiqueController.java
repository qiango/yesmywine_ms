package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.BoutiqueService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页精品闪购
 * Created by wangdiandian on 2017/6/1.
 */
@RestController
@RequestMapping("/cms/boutique")
public class BoutiqueController {

    @Autowired
    private BoutiqueService boutiqueService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer boutiqueFirstId) {   //查看
        if(ValueUtil.notEmpity(boutiqueFirstId)){
            return ValueUtil.toJson(boutiqueService.findOne(boutiqueFirstId));
        }
        return ValueUtil.toJson(boutiqueService.findAll());
    }

//    @RequestMapping(value = "/front", method = RequestMethod.GET)
//    public String frontIndex() {   //查看
//        return ValueUtil.toJson(boutiqueService.FrontfindAll());
//    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(String name, String jsonString) {   //新增
        try {
            ValueUtil.verify(name,"name");
//            ValueUtil.verify(jsonString,"jsonString");
            String insert = this.boutiqueService.insert(name, jsonString);
            if("success".equals(insert)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }
            return ValueUtil.toError("500", insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, String name, String jsonString) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(name,"name");
            String update = this.boutiqueService.update(id, name, jsonString);
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
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.boutiqueService.deleteFirst(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/secent", method = RequestMethod.DELETE)
    public String deleteSecent(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.boutiqueService.deleteSecent(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/getShuffling", method = RequestMethod.GET)
    public String getShuffling() {
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK, boutiqueService.getShuffling());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

}
