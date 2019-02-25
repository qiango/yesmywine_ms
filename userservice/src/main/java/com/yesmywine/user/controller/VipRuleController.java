package com.yesmywine.user.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.entity.VipRule;
import com.yesmywine.user.service.VipRuleService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by ${shuang} on 2016/12/12.
 */
@RestController
@RequestMapping("/userservice/vipRule")
public class VipRuleController {

    @Autowired
    private VipRuleService vipRuleService;


    @RequestMapping( method = RequestMethod.POST)
    public String index(@RequestParam Map<String, String> params ,HttpServletRequest request) {//插入规则
        Integer userId =null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        try {
            return vipRuleService.insert(params);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping( method = RequestMethod.DELETE)
    public String delete(@RequestParam Map<String, String> params,HttpServletRequest request) {//删除规则
        Integer userId =null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        try {
            return vipRuleService.delete(params);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping( method = RequestMethod.PUT)
    public String update(@RequestParam Map<String, String> params,HttpServletRequest request) {//更改规则
        Integer userId =null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        try {
            return vipRuleService.insert(params);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    //    @Description   查询列表
    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, String> params, Integer pageNo, Integer pageSize, Integer vipRuleId ,HttpServletRequest request) {
        MapUtil.cleanNull(params);
        Integer userId =null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        if(ValueUtil.isEmpity(vipRuleId)){
            if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                return ValueUtil.toJson(vipRuleService.findAll());
            }else  if(null!=params.get("all")){
                params.remove(params.remove("all").toString());
            }
            PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
            if (null != params.get("showFields")) {
                pageModel.setFields(params.remove("showFields").toString());
            }
            if (pageNo != null) params.remove(params.remove("pageNo").toString());
            if (pageSize != null) params.remove(params.remove("pageSize").toString());

            for (String key :params.keySet()) {
                if(ValueUtil.isEmpity(params.get(key))){
                    params.remove(key);
                }
            }
            pageModel.addCondition(params);
            pageModel = vipRuleService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
        }else {
            VipRule vipRule = vipRuleService.findOne(vipRuleId);
            return ValueUtil.toJson(HttpStatus.SC_OK,vipRule);
        }
    }


}
