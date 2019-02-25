package com.yesmywine.user.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.dao.BeanFlowDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.BeanFlow;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.service.BeanFlowService;
import com.yesmywine.user.service.UserLevelService;
import com.yesmywine.util.basic.*;
import com.yesmywine.util.error.YesmywineException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * Created by ${shuang} on 2017/4/12.
 */


@RestController
@RequestMapping("/userservice/beans")
public class BeanController {

    @Autowired
    private BeanFlowService beanFlowService;
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private BeanFlowDao beanFlowDao;
    @Autowired
    private UserLevelService userLevelService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,HttpServletRequest request) {
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
                return ValueUtil.toJson(beanFlowService.findAll());
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
            pageModel = beanFlowService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
    }



    @RequestMapping(value = "/synchronization",method = RequestMethod.POST)
    public String beanFlowSys(String jsonData){
        String  result = beanFlowService.beanFlowSys(jsonData);
        return  ValueUtil.toJson(HttpStatus.SC_CREATED,result);
    }

    @RequestMapping(value = "/syntopass",method = RequestMethod.POST)
    public String beanFlowSys(Integer beanFlowId){
        String  result = beanFlowService.syntopass(beanFlowId);
        return  result;
    }




    @RequestMapping(value="/online/itf",method = RequestMethod.POST)
    public String localGenerate(@RequestParam Map<String, String> params){//酒豆生成
        boolean isNunicodeDigits= StringUtils.isNumeric(params.get("point"));
        if(isNunicodeDigits==false){
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"积分格式不对", "point:"+params.get("point"));
        }
        String userId=params.get("userId");
        UserInformation userInformation = userInformationDao.findOne(Integer.valueOf(userId));
        String orderNumber=params.get("orderNumber");
        Integer point=Integer.valueOf(params.get("point"));
        String channelCode=params.get("channelCode");
        String userName=userInformation.getUserName();
        String phoneNumber=userInformation.getPhoneNumber();
        try {
            ValueUtil.verify(phoneNumber);
            ValueUtil.verify(point);
            ValueUtil.verify(channelCode);
            Double bean= beansCreate(userName, phoneNumber, orderNumber, point, channelCode);
            HashMap<String,Double> map=new HashMap<String, Double>();
            map.put("generate",bean);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,map);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());

            return ValueUtil.toError(e.getCode(),"转换失败，格式不对");
        }

    }

    public Double beansCreate(String userName, String phoneNumber, String orderNumber, Integer point, String channelCode) {
        UserInformation userInformation = userInformationDao.findByUserNameOrPhoneNumber(userName,phoneNumber);

        String json = this.http(channelCode);
        String mopo =ValueUtil.getFromJson(json,"mopo");//钱兑换积分
        String mobe =ValueUtil.getFromJson(json,"mobe");//钱兑换酒豆
        String rule = mopo;
        String[] rmbpoint = rule.split(":");
        BigDecimal rmb1 = new BigDecimal(Double.valueOf(rmbpoint[0]));
        BigDecimal point1 = new BigDecimal(Double.valueOf(rmbpoint[1]));
        BigDecimal point2 = new BigDecimal(Double.valueOf(point));//传入积分
        String proportion = mobe;
        String[] rmbBeans = proportion.split(":");
        BigDecimal rmb2 = new BigDecimal(Double.valueOf(rmbBeans[0]));
        BigDecimal beans = new BigDecimal(Double.valueOf(rmbBeans[1]));
        Double newRmb = (rmb1.multiply(point2)).divide(point1, 4, ROUND_HALF_DOWN).doubleValue();//兑换的人民币
        Double newBeans = (beans.multiply(BigDecimal.valueOf(newRmb))).divide(rmb2, 4, ROUND_HALF_DOWN).setScale(2,ROUND_HALF_DOWN).doubleValue();//兑换的酒豆
        BigDecimal bigDecimal = new BigDecimal(userInformation.getBean());
        BigDecimal bigDecimal1 = new BigDecimal(newBeans);
        Double newbeans = bigDecimal.add(bigDecimal1).doubleValue();//最后个人酒豆
        userInformation.setBean(newbeans);

        BeanFlow beanUserFlow = new BeanFlow();//个人酒豆生成记录
        beanUserFlow.setUserName(userInformation.getUserName());
        beanUserFlow.setOrderNumber(orderNumber);
        beanUserFlow.setBeans(newBeans);
        beanUserFlow.setPoints(point);
        beanUserFlow.setUserId(userInformation.getId());
        beanUserFlow.setDescription("订单号为"+orderNumber+"生成酒豆");
        beanUserFlow.setChannelCode(channelCode);
        beanUserFlow.setStatus("generate");
        beanUserFlow.setChannelName("官网");
        UserInformation userInformation1 =null;
        try {
            userInformation1 =userLevelService.vipUp(userInformation.getId(),point);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
             ValueUtil.toError(e.getCode(),e.getMessage());

        }

        userInformation.setVipRule(userInformation1.getVipRule());
        userInformation.setVoluntarily(userInformation1.getVoluntarily());
        userInformation.setGrowthValue(userInformation1.getGrowthValue());
        HashMap<String,Object> map =new HashMap<>();
        map.put("status","generate");
        map.put("userName",userInformation.getUserName());
        map.put("orderNumber",orderNumber);
        map.put("channelCode",channelCode);
        map.put("bean",newBeans);


        String centerFlowcode = SynchronizeUtils.paramsCode(Dictionary.PAAS_HOST,
                "/user/synchro", com.yesmywine.httpclient.bean.RequestMethod.post,map);

        String usercode = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,
                "/user/userInfo/syn",
                ValueUtil.toJson(userInformation), com.yesmywine.httpclient.bean.RequestMethod.post);

        String flowcode = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,
                "/user/beans/synchronization",
                ValueUtil.toJson(beanUserFlow), com.yesmywine.httpclient.bean.RequestMethod.post);

        if(ValueUtil.notEmpity(usercode)&&usercode.equals("201")){
            userInformation.setSynStatus(1);
        }else {
            userInformation.setSynStatus(0);//需要再次同步
        }

        if(ValueUtil.notEmpity(flowcode)&&flowcode.equals("201")){
            beanUserFlow.setSynStatus("1");
        }else {
            beanUserFlow.setSynStatus("0");//需要再次同步
        }

        userInformationDao.save(userInformation);
        beanFlowDao.save(beanUserFlow) ;
        return newBeans;

    }

    public String http(String channelCode){
        HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/user/rule/itf", com.yesmywine.httpclient.bean.RequestMethod.get);
        httpRequest.addParameter("channelCode", channelCode);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        String result = ValueUtil.getFromJson(temp, "data");
        return   result;
    }

}
