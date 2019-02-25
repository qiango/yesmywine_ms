package com.yesmywine.user.webController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.user.service.AppealService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
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
@RequestMapping("/member/userservice/appeal")
public class WebAppealController {

    @Autowired
    private AppealService appealService;

    //   列表
    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,HttpServletRequest request) {
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        params.put("userId",userId);
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
        //上诉
    @RequestMapping(method = RequestMethod.POST)
    public String appeal(String justification, HttpServletRequest request ){
        Integer userId = null;
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
            return  appealService.appeal(userId,justification);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

}
