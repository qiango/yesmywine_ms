package com.yesmywine.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.user.dao.BeanFlowDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.BeanFlow;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.service.BeanFlowService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by ${shuang} on 2017/4/12.
 */
@Service
public class BeanFlowServiceImpl  extends BaseServiceImpl<BeanFlow,Integer> implements BeanFlowService {
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private BeanFlowDao beanFlowDao;


    @Override
    public String consume(Integer userId, Integer bean,String orderNumber,String channelCode) throws YesmywineException {
        UserInformation userInformation=userInformationDao.findOne(userId);
        BigDecimal bigDecimal3 = new BigDecimal(userInformation.getBean());
        BigDecimal bigDecimal4 = new BigDecimal(bean);
        //计算消耗的酒豆数量是否合法
        Double result2 = bigDecimal3.subtract(bigDecimal4).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
        if(result2<0){
            return "豆子不足" ;
        }
        userInformation.setBean(result2);
        //酒豆消耗记录
        BeanFlow beanFlow=new BeanFlow();
        beanFlow.setBeans(bean.doubleValue());
        beanFlow.setPoints(0);
        beanFlow.setUserId(userId);
        beanFlow.setOrderNumber(orderNumber);
        beanFlow.setStatus("consume");
        beanFlow.setUserName(userInformation.getUserName());
        beanFlow .setDescription("订单号"+orderNumber);
        beanFlow.setChannelCode(channelCode);

        //同步到paas需要传递的参数
        HashMap<String,Object> map =new HashMap<>();
        map.put("status","consume");
        map.put("userName",userInformation.getUserName());
        map.put("orderNumber",orderNumber);
        map.put("channelCode",channelCode);
        map.put("bean",bean);


        String centerFlowcode = SynchronizeUtils.paramsCode(Dictionary.PAAS_HOST,
                "/user/synchro", RequestMethod.post,map);

//        同步消耗酒豆到pass
        String code = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,"/user/beans/synchronization",ValueUtil.toJson(beanFlow),RequestMethod.post);

//        同步用户信息到pass的用户中心
        String code1 = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,"/user/userInfo/syn", ValueUtil.toJson(userInformation),RequestMethod.post);

//        如果同步失败状态0，则需要手动同步，如果成功则为1不需要同步
        if(ValueUtil.notEmpity(code1)&&code1.equals("201")){
            userInformation.setSynStatus(1);
        }else {
            userInformation.setSynStatus(0);
        }
        if(ValueUtil.notEmpity(code)&&code.equals("201")){
            beanFlow.setSynStatus("1");
        }else {
            beanFlow.setSynStatus("0");
        }
        beanFlowDao.save(beanFlow);
        userInformationDao.save(userInformation);
        return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
    }

//    接收来自paas的酒豆记录同步
    @Override
    public String beanFlowSys(String jsonData) {
        JSONObject jsonObject = JSON.parseObject(jsonData);
        JSONObject beanFlowJson = jsonObject.getJSONObject("data");
        String userName = beanFlowJson.getString("userName");
        String userId = beanFlowJson.getString("userId");
        String beans = beanFlowJson.getString("beans");
        String point = beanFlowJson.getString("point");
        String status = beanFlowJson.getString("status");
        String orderNumber = beanFlowJson.getString("orderNumber");
        JSONObject channel = beanFlowJson.getJSONObject("channels");
        String channelName = channel.getString("channelName");
        String channelCode = channel.getString("channelCode");

        BeanFlow beanFlow =new BeanFlow();
        beanFlow.setBeans(Double.valueOf(beans));
        beanFlow.setOrderNumber(orderNumber);
        beanFlow.setUserName(userName);
        beanFlow.setPoints(Integer.parseInt(point));
        beanFlow.setChannelName(channelName);
        beanFlow.setUserId(Integer.parseInt(userId));
        beanFlow.setChannelCode(channelCode);
        beanFlow.setStatus(status);
        beanFlowDao.save(beanFlow);
        return "success";
    }

    @Override
    public String syntopass(Integer beanuserId) {
        BeanFlow beanFlow = beanFlowDao.findOne(beanuserId);
        //        同步消耗酒豆明细到pass
        String code = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,"/user/beans/synchronization",ValueUtil.toJson(beanFlow),RequestMethod.post);
        if(ValueUtil.notEmpity(code)&&code.equals("201")){
            return ValueUtil.toJson(HttpStatus.SC_OK,"同步成功");
        }else {
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"同步失败");
        }

    }

}
