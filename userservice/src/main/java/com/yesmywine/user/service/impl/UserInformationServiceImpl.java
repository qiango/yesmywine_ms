
package com.yesmywine.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.bean.ConstantData;
import com.yesmywine.user.dao.BeanFlowDao;
import com.yesmywine.user.dao.UserEmailDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.dao.VipRuleDao;
import com.yesmywine.user.entity.BeanFlow;
import com.yesmywine.user.entity.UserEmail;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.entity.VipRule;
import com.yesmywine.user.service.UserInformationService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by light on 2017/2/10.
 */
@Service
public class UserInformationServiceImpl extends BaseServiceImpl<UserInformation,Integer> implements UserInformationService {


    @Override
    public List<UserInformation> findAll() {
        return userInformationDao.findAll();
    }

    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private VipRuleDao vipRuleDao;
    @Autowired
    private BeanFlowDao beanFlowDao;
    @Autowired
    private UserEmailDao userEmailDao;


    @Override
    public UserInformation findByUserId(Integer userId) {
        return userInformationDao.findOne(userId);
    }

    @Override
    public String initalize(Integer userId) throws YesmywineException {
//        初始化用户等级
      UserInformation userInformation = userInformationDao.findOne(userId);
        Integer minId=vipRuleDao.findMinId();//等级Id
        VipRule vipRule=vipRuleDao.findOne(minId);
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
        Date date = curr.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//转换格式
        String sys = sdf.format(date);
        userInformation.setVoluntarily(sys);
        userInformation.setVipRule(vipRule);
        userInformationDao.save(userInformation);
        return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformation);
    }



    @Override
    public UserInformation findByUserName(String userName) {
        return userInformationDao.findByUserName(userName);
    }


    public String saveUserImg(Integer userId, Integer[] imgIds) throws YesmywineException {
//        保存用户图片
        try{
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/fileUpload/tempToFormal/itf", RequestMethod.post);
            httpRequest.addParameter("module", "userservice");
            httpRequest.addParameter("mId", userId);
            httpRequest.addParameter("type", "1");
            String ids = "";
            String imageIds = "";
            for (int i = 0; i < imgIds.length; i++) {
                if (i == 0) {
                    ids = ids + imgIds[i];
//                    imageIds=imageIds+imageId[i];
                } else {
                    ids = ids + "," + imgIds[i];
//                    imageIds=imageIds+":"+imageId[i];
                }
//                category.setImageId(imageIds);
                httpRequest.addParameter("id", ids);
            }
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String cd = ValueUtil.getFromJson(temp, "code");
            if (!"201".equals(cd) || ValueUtil.isEmpity(cd)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("图片上传失败");
            } else {
                JSONArray maps = new JSONArray(imgIds.length);
                String result = ValueUtil.getFromJson(temp, "data");
                JsonParser jsonParser = new JsonParser();
                JsonArray image = jsonParser.parse(result).getAsJsonArray();
                for (int f = 0; f < image.size(); f++) {
                    String id = image.get(f).getAsJsonObject().get("id").getAsString();
                    String name = image.get(f).getAsJsonObject().get("name").getAsString();
                    com.alibaba.fastjson.JSONObject map1 = new com.alibaba.fastjson.JSONObject();
                    map1.put("id", id);
                    map1.put("name", name);
                    maps.add(map1);
                }

                String result1 =   maps.toJSONString().replaceAll( "\"", "\'");

//                com.alibaba.fastjson.JSONObject map = new com.alibaba.fastjson.JSONObject();
//                for (int i = 0; i < maps.size(); i++) {
//                    com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) maps.get(i);
//                    map.put("id" + i, jsonObject.getString("id"));
//                    map.put("name" + i, jsonObject.getString("name"));
//                }
//                map.put("num", String.valueOf(maps.size()));
                return result1;
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError("图片服务出现问题！");
        }
        return null;
    }

    @Override
    public String update(Map<String,String> map,HttpServletRequest request) throws YesmywineException {
//        更新密码
        Integer userId=UserUtils.getUserId(request);
        String oldPassword=map.get("oldPassword");
        String newPasswordFirst=map.get("newPasswordFirst");
        UserInformation userInformation=userInformationDao.findOne(userId);
        String password=userInformation.getPassword();
        if(!password.equals(oldPassword)){
            ValueUtil.isError("输入旧密码有误");
        }else {
            String code=httpcl(userInformation);
            if(code.equals("201")){
                userInformation.setPassword(newPasswordFirst);//加密
                userInformationDao.save(userInformation);
                return "success";
            }else {
                ValueUtil.isError("同步失败");
            }
        }
        return null;
    }

    public String message(HttpServletRequest request,String phone){//短信点击
        HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/sms/send/sendSms/itf",RequestMethod.post);
        httpRequest.addParameter("phones",phone);
        httpRequest.addParameter("code",ConstantData.code);
        httpRequest.run();
        String json = httpRequest.getResponseContent();
        String messages=ValueUtil.getFromJson(json,"data");
        RedisCache.set(phone,messages,6000);
//        System.out.print("验证码为"+RedisCache.get("message"));
//        HttpSession session = request.getSession();
//        session.setAttribute("message",messages);
        return "success";
    }

    public String messageForget(HttpServletRequest request,String phone){//
        HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/sms/send/sendSms/itf",RequestMethod.post);
        httpRequest.addParameter("phones",phone);
        httpRequest.addParameter("code",ConstantData.codeForget);
        httpRequest.run();
        String json = httpRequest.getResponseContent();
        String messages=ValueUtil.getFromJson(json,"data");
        RedisCache.set(phone,messages,6000);
//        HttpSession session = request.getSession();
//        session.setAttribute("message",messages);
        return "success";
    }

    public String bindingPhone(HttpServletRequest request,String message,String phone)throws YesmywineException {//绑定手机确定
        UserInformation u=userInformationDao.findByPhoneNumber(phone);
        if(null!=u){
            ValueUtil.isError("该手机号已经被绑定");
        }
        String oldMessage=RedisCache.get(phone);
//        HttpBean httpRequest = new HttpBean(ConstantData.captcha + "/getCode",RequestMethod.get);
//        httpRequest.addParameter("captcha",captcha);
//        httpRequest.run();
//        String json = httpRequest.getResponseContent();
//        String code=ValueUtil.getFromJson(json,"code");
        if(!oldMessage.equals(message)){
            ValueUtil.isError("短信验证错误");
        }else {
            Integer userId = UserUtils.getUserId(request);
            UserInformation userInformation = userInformationDao.findOne(userId);
            userInformation.setPhoneNumber(phone);
            userInformation.setBindPhoneFlag(true);
            String codess=httpcl(userInformation);
            if(codess.equals("201")){
                userInformationDao.save(userInformation);
                return "success";
            }else {
                ValueUtil.isError("同步失败！");
            }

        }
        return null;
    }

    public String updateFirst(HttpServletRequest request,String message,String phone)throws YesmywineException {//输入验证码和短信验证码点击下一步
//        HttpSession session = request.getSession();
//        String oldMessage=session.getAttribute("message").toString();
        String oldMessage=RedisCache.get(phone);
        if(null==oldMessage){
            ValueUtil.isError("验证码已过期");
        }
        if(!oldMessage.equals(message)){
            ValueUtil.isError("短信验证错误");
        }
        return "success";
    }

    public String updatePassword(String passwordFirst,String phone)throws YesmywineException {//修改登录密码
        UserInformation userInformation=userInformationDao.findByPhoneNumber(phone);
        if(null==userInformation){
            ValueUtil.isError("该手机号未注册");
        }else
            userInformation.setPassword(passwordFirst);
        String code=httpcl(userInformation);
        if(code.equals("201")){
            userInformationDao.save(userInformation);
            return "success";
        }else {
            ValueUtil.isError("同步失败！");
        }
        return null;
    }

    @Override
    public String updatePhone(HttpServletRequest request, String message,String phone) throws YesmywineException {
//        HttpSession session = request.getSession();
        String oldMessage=RedisCache.get(phone);
//        String oldMessage = session.getAttribute("message").toString();
        if(ValueUtil.isEmpity(oldMessage)){
            ValueUtil.isError("验证码过期");
        }
        if (!oldMessage.equals(message)) {
            ValueUtil.isError("短信验证错误");
        }
        return "success";
    }

    @Override
    public String updateResgister(HttpServletRequest request, String message,String email) throws YesmywineException {
        Integer userId = UserUtils.getUserId(request);
        String oldMessage = RedisCache.get(email);
        if(ValueUtil.isEmpity(oldMessage)){
            ValueUtil.isError("验证码过期");
        }
        if (!oldMessage.equals(message)) {
            ValueUtil.isError("邮件验证码错误");
        }
        UserEmail userEmail=userEmailDao.findByUserId(userId);
        UserInformation userInformation = userInformationDao.findOne(userId);
        userInformation.setEmail(userEmail.getEmail());
        userInformation.setBindEmailFlag(true);
        userInformationDao.save(userInformation);
        userEmailDao.delete(userEmail);
        return "success";
    }


    public String payPassword(HttpServletRequest request,String payPasswordFirst)throws YesmywineException {//设置支付密码
        Integer userId = UserUtils.getUserId(request);
        UserInformation userInformation = userInformationDao.findOne(userId);
        userInformation.setPaymentPassword(payPasswordFirst);
        String code=httpcl(userInformation);
        if(code.equals("201")){
            userInformationDao.save(userInformation);
            return "success";
        }else {
            ValueUtil.isError("同步失败！");
        }
        return null;
    }

    public String updatePayPassword(HttpServletRequest request,String oldPassword,String payPasswordFirst)throws YesmywineException {//修改支付密码
        Integer userId = UserUtils.getUserId(request);
        UserInformation userInformation = userInformationDao.findOne(userId);
        if(!oldPassword.equals(userInformation.getPaymentPassword())){
            ValueUtil.isError("输入的原密码错误");
        }else
        userInformation.setPaymentPassword(payPasswordFirst);
        String code=httpcl(userInformation);
        if(code.equals("201")){
            userInformationDao.save(userInformation);
            return "success";
        }else {
            ValueUtil.isError("同步失败！");
        }
        return null;
    }

    public String resetPayPassword(HttpServletRequest request,String phone)throws YesmywineException {//重置支付密码
        HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/sms/send/sendSms/itf",RequestMethod.post);
        httpRequest.addParameter("phones",phone);
        httpRequest.addParameter("code",ConstantData.code);
        httpRequest.run();
        String json = httpRequest.getResponseContent();
        String messages=ValueUtil.getFromJson(json,"data");
        Integer userId = UserUtils.getUserId(request);
        UserInformation userInformation = userInformationDao.findOne(userId);
        userInformation.setPaymentPassword(messages);
        String code=httpcl(userInformation);
        if(code.equals("201")){
            userInformationDao.save(userInformation);
            return "success";
        }else {
            ValueUtil.isError("同步失败！");
        }
        return null;
    }

    public  static String httpcl(UserInformation userInformation){
        String code= SynchronizeUtils.getCode(Dictionary.PAAS_HOST,"/user/userInfo/syn", ValueUtil.toJson(userInformation),RequestMethod.post);
        return code;
    }

    public String localConsume(Map<String, String> params, Integer userId) {
        Integer bean = Integer.valueOf(params.get("consumeBean"));
        String orderNumber = params.get("orderNumber");
        String channelCode = params.get("channelCode");
        UserInformation userInformation=userInformationDao.findOne(userId);
        BigDecimal bigDecimal3 = new BigDecimal(userInformation.getBean());
        BigDecimal bigDecimal4 = new BigDecimal(bean);
        Double result2 = bigDecimal3.subtract(bigDecimal4).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
        if(result2<0){
            return "豆子不足" ;
        }
        BeanFlow beanFlow=new BeanFlow();
        beanFlow.setBeans(bean.doubleValue());
        beanFlow.setPoints(0);
        beanFlow.setUserId(userId);
        beanFlow.setOrderNumber(orderNumber);
        beanFlow.setSynStatus("消耗");
        beanFlow.setUserName(userInformation.getUserName());
        beanFlow.setDescription(channelCode+"消耗");
        beanFlowDao.save(beanFlow);
        return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
    }

    @Override
    public String saveOrUpdate(String jsonData) {
        JSONObject jsonObject = JSON.parseObject(jsonData);
        JSONObject userJson = jsonObject.getJSONObject("data");
        String id = userJson.getString("userId");
        String userName = userJson.getString("userName");
        String phoneNumber = userJson.getString("phoneNumber");
        String bindPhoneFlag = userJson.getString("bindPhoneFlag");
        String nickName = userJson.getString("nickName");
        String email = userJson.getString("email");
        String bindEmailFlag = userJson.getString("bindEmailFlag");
        String IDCardNum = userJson.getString("IDCardNum");
        String bean = userJson.getString("bean");
        String registerChannel = userJson.getString("registerChannel");
        String growthValue = userJson.getString("growthValue");
        String remainingSum = userJson.getString("remainingSum");
        String voluntarily = userJson.getString("voluntarily");
        JSONObject vipRuleObject = userJson.getJSONObject("vipRule");
        String levelId = vipRuleObject.getString("mallId");
        VipRule vipRule = vipRuleDao.findOne(Integer.valueOf(levelId));

        UserInformation userInformation = userInformationDao.findByUserName(userName);

        if(userInformation==null){
            userInformation = new UserInformation();
            userInformation.setUserName(phoneNumber);
            userInformation.setPhoneNumber(phoneNumber);
            userInformation.setBindPhoneFlag(Boolean.valueOf(bindPhoneFlag));
            userInformation.setNickName(nickName);
            userInformation.setEmail(email);
            userInformation.setBindEmailFlag(Boolean.valueOf(bindEmailFlag));
            userInformation.setIDCardNum(IDCardNum);
            userInformation.setBean(Double.valueOf(bean));
            userInformation.setRegisterChannel(registerChannel);
            userInformation.setGrowthValue(Integer.valueOf(growthValue));
            userInformation.setRemainingSum(Double.valueOf(remainingSum));
            userInformation.setVoluntarily(voluntarily);
            userInformation.setVipRule(vipRule);
        }else{
            userInformation.setUserName(userName);
            userInformation.setPhoneNumber(phoneNumber);
            userInformation.setBindPhoneFlag(Boolean.valueOf(bindPhoneFlag));
            userInformation.setNickName(nickName);
            userInformation.setEmail(email);
            userInformation.setBindEmailFlag(Boolean.valueOf(bindEmailFlag));
            userInformation.setIDCardNum(IDCardNum);
            userInformation.setBean(Double.valueOf(bean));
            userInformation.setRegisterChannel(registerChannel);
            userInformation.setGrowthValue(Integer.valueOf(growthValue));
            userInformation.setRemainingSum(Double.valueOf(remainingSum));
            userInformation.setVoluntarily(voluntarily);
            userInformation.setVipRule(vipRule);
        }
        userInformationDao.save(userInformation);
        return "success";
    }



}
