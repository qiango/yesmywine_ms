package com.yesmywine.user.webController;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.bean.ConstantData;
import com.yesmywine.user.dao.SignFlowDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.SignFlow;
import com.yesmywine.user.service.SignFlowService;
import com.yesmywine.util.basic.Dictionary;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * Created by ${shuang} on 2017/7/11.
 */

@RestController
@RequestMapping("/member/userservice/sign")
public class WebSignPointController {

    @Autowired
    private SignFlowDao signFlowDao;
    @Autowired
    private SignFlowService signFlowService;
    @Autowired
    private UserInformationDao userInformationDao;
    @RequestMapping(method = RequestMethod.POST)
    public String sign( HttpServletRequest request ){
        Integer userId = null;
        String point= null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录");
            }
            SignFlow signFlow1 = signFlowDao.findByUserIdAndSignTime(userId,df.format(new Date()));
            if(ValueUtil.notEmpity(signFlow1)){
                return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"您已签到过了");
            }
            String name = userInformationDao.findOne(userId).getUserName();
               if(this.isToDaySend100(userId.longValue())){
                   point= this.http("maxPoint");
                   SignFlow signFlow = new SignFlow();
                   signFlow.setPoint(Integer.parseInt(point));
                   signFlow.setUserId(userId);
                   signFlow.setSignTime(df.format(new Date()).toString());
                   signFlow.setUserName(name);
                   signFlowDao.save(signFlow);
               }else {
                   point = this.getRandomScore()+"";
                   SignFlow signFlow = new SignFlow();
                   signFlow.setPoint(Integer.parseInt(point));
                   signFlow.setUserId(userId);
                   signFlow.setSignTime(df.format(new Date()).toString());
                   signFlow.setUserName(name);
                   signFlowDao.save(signFlow);
               }
            return ValueUtil.toJson(HttpStatus.SC_CREATED,point);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

    }
    private boolean isToDaySend100(Long memberId){
        Calendar c = Calendar.getInstance();
        int currentDayByWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        c.set(Calendar.DAY_OF_WEEK, 1);
        int w = c.get(Calendar.WEEK_OF_YEAR); // 0- 55
        int date = c.get(Calendar.DATE);
        int m = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_YEAR);
        //memberId : 1--
        long res = (int)(date + day*2.2 + w + m*5.5 + memberId*0.677);
        int dayByWeek = (int)res % 7;
        return currentDayByWeek == dayByWeek;
    }

    private int getRandomScore(){
        Integer start = Integer.parseInt(this.http("pointStart"));
        Integer end =  Integer.parseInt(this.http("pointEnd"));
        Random random = new Random();
        return random.nextInt(end-start+1) + start;
    }

    public String http(String sysCode){
        HttpBean httpBean = new HttpBean(Dictionary.PAAS_HOST+"/dic/sysCode/itf", com.yesmywine.httpclient.bean.RequestMethod.get);
        httpBean.addParameter("sysCode", sysCode);
        httpBean.run();
        String temp = httpBean.getResponseContent();
        String value = ValueUtil.getFromJson(temp, "data");
        JSONArray jsonArray = JSON.parseArray(value);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
      String result =  jsonObject.get("entityValue").toString();
        return result;
    }

    // 查询签到记录
    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,HttpServletRequest request) {
        MapUtil.cleanNull(params);
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError( "未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        params.put("userId",userId);
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
            pageModel = signFlowService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
    }

    @RequestMapping(value = "/check",method = RequestMethod.GET)
    public String check(HttpServletRequest request){
        Integer userId = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError( "未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        SignFlow signFlow1 = signFlowDao.findByUserIdAndSignTime(userId,df.format(new Date()));
        if(ValueUtil.notEmpity(signFlow1)){
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"您已签到过了");
        }else {
            return ValueUtil.toJson(HttpStatus.SC_OK,"可签到");
        }

    }

}
