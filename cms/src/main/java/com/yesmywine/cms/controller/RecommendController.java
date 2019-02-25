
package com.yesmywine.cms.controller;


import com.yesmywine.cms.service.RecommendService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 精品推荐
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/cms/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer id) {   //查看
        if(ValueUtil.notEmpity(id)){
            return ValueUtil.toJson(recommendService.findOne(id));
        }
        return ValueUtil.toJson(recommendService.findAll());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(String name,Integer userId, String jsonString,String reasons) {   //新增
        try {
            ValueUtil.verify(name,"name");
            String insert = this.recommendService.insert(name, userId, jsonString, reasons);
            if("success".equals(insert)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }
            return ValueUtil.toError("500", insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, String name, Integer userId, String jsonString,String reasons) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(name,"name");
            String update = this.recommendService.update(id, name, userId, jsonString, reasons);
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
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.recommendService.deleteFirst(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/secent", method = RequestMethod.DELETE)
    public String deleteSecent(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.recommendService.deleteSecent(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}