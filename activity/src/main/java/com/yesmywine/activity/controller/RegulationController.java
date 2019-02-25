package com.yesmywine.activity.controller;

import com.yesmywine.activity.entity.IftttRegulation;
import com.yesmywine.activity.service.RegulationService;
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
 * Created by SJQ on 2017/5/10.
 */
@RestController
@RequestMapping("/activity/regulation")
public class RegulationController {

    @Autowired
    private RegulationService regulationService;

    /*
    *@Author Gavin
    *@Description 创建规则
    *@Date 2017/5/10 15:54
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam Map<String, String> param) {
        try {
            String result = regulationService.create(param);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, result);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 修改规则
    *@Date 2017/5/10 15:54
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@RequestParam Map<String, String> param) {
        try {
            String result = regulationService.update(param);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, result);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 删除规则
    *@Date 2017/5/10 15:54
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer id) {
        try {
            String result = regulationService.delete(id);
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, result);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 规则列表及详情
    *@Date 2017/5/10 15:54
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer activityId,Integer regulationId) {
        if(regulationId!=null){
            return ValueUtil.toJson(regulationService.getOne(regulationId));
        }
        List<IftttRegulation> regulationList = regulationService.findByActivityIdNoCache(activityId);
        return ValueUtil.toJson( regulationList);
    }

}
