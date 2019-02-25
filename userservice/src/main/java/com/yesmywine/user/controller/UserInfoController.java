
package com.yesmywine.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.dao.BeanFlowDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.BeanFlow;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.service.UserInformationService;
import com.yesmywine.user.service.UserLevelService;
import com.yesmywine.util.basic.*;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.util.number.DoubleUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
public class UserInfoController {
    @Autowired
    private UserInformationService userInformationService;
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private BeanFlowDao beanFlowDao;
    @Autowired
    private UserLevelService userLevelService;

//  @Description   查询用户列表
    @RequestMapping(value = "/userservice/userInfomation",method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,HttpServletRequest request,Integer id) {
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
        if(ValueUtil.notEmpity(id)){
            params.put("id",id);
        }
        Object channelType = params.get("channelType");
        if(ValueUtil.notEmpity(channelType)){
            params.remove(params.remove("channelType").toString());
            params.put("channelType",channelType);
        }
            MapUtil.cleanNull(params);
            if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                return ValueUtil.toJson(userInformationService.findAll());
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
            pageModel = userInformationService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
    }


    @RequestMapping(value = "/userservice/userInfomation/passsys", method = RequestMethod.POST)
    public String sys(@RequestParam Map<String,String> params,Integer userId){
        try {
            ValueUtil.verify(params.get("userId"));
            ValueUtil.verify(params.get("consumeBean"));
            ValueUtil.verify(params.get("channelCode"));
            ValueUtil.verify(params.get("orderNumber"));
            ValueUtil.verify(userId);
            return userInformationService.localConsume(params,userId);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/userservice/userInfomation/synchronization", method = RequestMethod.POST)//接收pass层的同步
    public  String synchronization(String jsonData){

      String  result = userInformationService.saveOrUpdate(jsonData);
        return  ValueUtil.toJson(HttpStatus.SC_CREATED,result);
    }

    @RequestMapping(value = "/userservice/userInfomation/changeBeansAndGrowthValue", method = RequestMethod.POST)
    public  String changeBeansAndGrowthValue(String jsonData) {
        try {
            JSONObject json = JSON.parseObject(jsonData);
            String userId = json.getString("userId");
            String growthValue = json.getString("growthValue");
            String beans = json.getString("beans");
            String type = json.getString("type");
            String orderNumber = json.getString("orderNumber");

            ValueUtil.verify(userId, "userId");
            UserInformation user = userInformationService.findOne(Integer.valueOf(userId));

            if (user == null) {
                ValueUtil.isError("无此用户！");
            }

            if (growthValue != null && NumberUtils.isNumber(growthValue)) {
                user.setGrowthValue(user.getGrowthValue() + Integer.valueOf(growthValue));
            }
            if (beans != null && NumberUtils.isNumber(beans)) {
                user.setBean(DoubleUtils.add(user.getBean(), Double.valueOf(beans)));
            }
            BeanFlow beanFlow = new BeanFlow();
            beanFlow.setBeans(Double.valueOf(beans));
            beanFlow.setPoints(Integer.valueOf(growthValue));
            if(ValueUtil.isEmpity(orderNumber)){
                beanFlow.setDescription("赠送酒豆");
            }else {
                beanFlow.setDescription("订单"+orderNumber+"赠送酒豆");
                beanFlow.setOrderNumber(orderNumber);
            }
            beanFlow.setUserName(user.getUserName());
            beanFlow.setUserId(user.getId());
            beanFlowDao.save(beanFlow);
            UserInformation userInformation1 = userLevelService.vipUp(Integer.valueOf(userId),Integer.valueOf(growthValue));
            user.setVipRule(userInformation1.getVipRule());
            user.setVoluntarily(userInformation1.getVoluntarily());
            String code = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,"/user/userInfo/syn", ValueUtil.toJson(HttpStatus.SC_CREATED,user), com.yesmywine.httpclient.bean.RequestMethod.post);

            if(ValueUtil.notEmpity(code)&&code.equals("201")){
                user.setSynStatus(1);
            }else {
                user.setSynStatus(0);
            }
            userInformationService.save(user);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, "SUCCESS");

        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/userservice/userInfomation/show/itf", method = RequestMethod.GET)//查找一个
    public  String showItf(Integer userId){
        UserInformation userInformation   = userInformationDao.findOne(userId);
        return  ValueUtil.toJson(HttpStatus.SC_OK,userInformation);
    }

    @RequestMapping(value = "/userservice/userInfomation/initalize/itf", method = RequestMethod.PUT)//初始化
    public  String initalize(Integer userId){
        String userInformation   = null;
        try {
            userInformation = userInformationService.initalize(userId);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        return  userInformation;
    }
}

