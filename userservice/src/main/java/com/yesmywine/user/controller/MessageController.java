package com.yesmywine.user.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.dao.MessageDao;
import com.yesmywine.user.service.NoticesService;
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
 * Created by ${shuang} on 2017/6/30.
 */

@RestController
@RequestMapping("/userservice/message")
public class MessageController {

     @Autowired
     private NoticesService noticesService;
     @Autowired
     private MessageDao messageDao;

    @RequestMapping( value = "/itf",method = RequestMethod.POST)//新增
    public String create(@RequestParam Map<String, String> params){
        try {
            ValueUtil.verify(params.get("goodsName"));
            ValueUtil.verify(params.get("orderNumber"));
            ValueUtil.verify(params.get("goodsImageUrl"));
            ValueUtil.verify( params.get("LogisticsNumber"));
            ValueUtil.verify(params.get("LogisticsName"));
            ValueUtil.verify(params.get("userId"));
            ValueUtil.verify(params.get("orderId"));
            ValueUtil.verify(params.get("goodsId"));
            String result = noticesService.create(params,Integer.valueOf(params.get("userId")));
            return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,HttpServletRequest request) {
        String userInfo = null;
        try {
            userInfo = UserUtils.getUserInfo(request).toJSONString();
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        if(ValueUtil.isEmpity(userInfo)){
            return "未登录";
        }
        MapUtil.cleanNull(params);
            if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                return ValueUtil.toJson(noticesService.findAll());
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
            pageModel = noticesService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
    }


}
