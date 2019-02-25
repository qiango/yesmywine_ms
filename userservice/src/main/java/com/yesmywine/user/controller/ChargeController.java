
package com.yesmywine.user.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.user.service.ChargeService;
import com.yesmywine.util.basic.MapUtil;
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
 * Created by ${shuang} on 2017/4/5.
 */
@RestController
@RequestMapping("/userservice/charge")
public class ChargeController {

    @Autowired
    private   ChargeService chargeService;


    @RequestMapping(value = "/web",method = RequestMethod.POST)
    public String charge(@RequestParam Map<String, String> params){//充值
        try {
            return ValueUtil.toJson(chargeService.charge(params));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

//    列表
    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,HttpServletRequest request) {
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
        MapUtil.cleanNull(params);
            if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                return ValueUtil.toJson(chargeService.findAll());
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
            pageModel = chargeService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
    }


}
