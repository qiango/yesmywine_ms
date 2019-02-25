
package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 广告页
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/cms/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer id) {   //查看
        if(ValueUtil.notEmpity(id)){
            return ValueUtil.toJson(activityService.findOne(id));
        }
        return ValueUtil.toJson(activityService.findAll());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(String pageJsonString, String appJsonString, Integer id, String columnJsonString,
                         Integer columnId, String goodsJsonString) {   //新增
        try {
            String insert = this.activityService.insert(pageJsonString, appJsonString, id,
                    columnJsonString, columnId, goodsJsonString);
            if("success".equals(insert)) {
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }
            return ValueUtil.toError("500", insert);
        } catch (Exception e) {
            return ValueUtil.toError("500", "erro");
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, String pageJsonString, String appJsonString, Integer columnId, String columnJsonString
            ,Integer activitySecentId, String goodsJsonString) {   //修改
        try {
            String s = this.activityService.update(id, pageJsonString, appJsonString
                    , columnId, columnJsonString, activitySecentId, goodsJsonString);
            if("success".equals(s)) {
                return ValueUtil.toJson(HttpStatus.SC_CREATED, s);
            }
            return ValueUtil.toError("500", s);
        } catch (Exception e) {
            return ValueUtil.toError("500", "erro");
        }
    }


    @RequestMapping(value = "/first", method = RequestMethod.DELETE)
    public String deleteFirst(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.activityService.deleteFirst(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/column", method = RequestMethod.DELETE)
    public String deleteColumn(String ids) {
        try {
            ValueUtil.verify(ids,"ids");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.activityService.deleteColumn(ids));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/secent", method = RequestMethod.DELETE)
    public String deleteSecent(String ids) {
        try {
            ValueUtil.verify(ids,"ids");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.activityService.deleteSecent(ids));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}