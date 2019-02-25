package com.yesmywine.user.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.entity.Appeal;
import com.yesmywine.user.service.AppealService;
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
 * Created by ${shuang} on 2017/4/6.
 */

@RestController
@RequestMapping("/userservice/appeal")
public class AppealController {

    @Autowired
    private AppealService appealService;

    //   列表
    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,HttpServletRequest request,Integer id) {
        if(ValueUtil.notEmpity(id)){
            Appeal appeal = appealService.findOne(id);
            return ValueUtil.toJson(HttpStatus.SC_OK,appeal);
        }
        MapUtil.cleanNull(params);
        String userInfo = null;
        try {
            userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
            if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                return ValueUtil.toJson(appealService.findAll());
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
            pageModel = appealService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
    }


    //处理申诉
    @RequestMapping( value="/feedback",method = RequestMethod.PUT)//提交申诉处理结果
    public String feedback(Integer userId,String feedback,Integer blackStatus,HttpServletRequest request ){
        String userInfo = null;
        try {
            userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        try {
            return   appealService.feedback(userId,feedback,blackStatus);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}
