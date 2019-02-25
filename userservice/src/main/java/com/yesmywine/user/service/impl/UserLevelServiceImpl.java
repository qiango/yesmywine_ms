package com.yesmywine.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.user.bean.ConstantData;
import com.yesmywine.user.dao.LevelHistoryDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.dao.VipRuleDao;
import com.yesmywine.user.entity.LevelHistory;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.entity.VipRule;
import com.yesmywine.user.service.UserLevelService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ${shuang} on 2016/12/12.
 */
@Service
public class UserLevelServiceImpl extends BaseServiceImpl<UserInformation,Integer> implements UserLevelService {

    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private LevelHistoryDao levelHistoryDao;
    @Autowired
    private VipRuleDao vipRuleDao;

    @Override
    public UserInformation vipUp(Integer userId, Integer growthValue) throws YesmywineException {//用户升级
        ValueUtil.verify(userId);
        ValueUtil.verify(growthValue);
//        Integer maxRequire=vipRuleDao.findByRequire();//最大等级要求分
        UserInformation userInformation=userInformationDao.findOne(userId);
        Integer currentId = userInformation.getVipRule().getId();//当前用户等级Id
        if(currentId==0){
            currentId = vipRuleDao.findMinId();
        }
        Integer oldGrowthValue = userInformation.getGrowthValue();//当前的成长值
        Integer newGrowthValue = growthValue+oldGrowthValue;//新的成长值
        Integer require=vipRuleDao.findOne(currentId).getRequireValue();//当前等级要求分
        List<VipRule> list=vipRuleDao.findMax(require);//大于当前等级的所有集合
        if(list.size()==0){//说明等级已最大
            userInformation.setGrowthValue(newGrowthValue);
            userInformation.setVoluntarily(this.getnextyear());
            LevelHistory levelHistory=new LevelHistory();
            levelHistory.setUserId(userId);
            levelHistory.setUserName(userInformation.getUserName());
            levelHistory.setRemarks("等级最大");
            levelHistoryDao.save(levelHistory);
        }else {//等级不是最大的
            VipRule vipRule=list.get(0);
            Integer newId=vipRule.getId();//新的Id
            Integer newRequire=vipRule.getRequireValue();
            if(newRequire<=newGrowthValue) {//进行升级
                userInformation.setVipRule(vipRule);
                userInformation.setGrowthValue(newGrowthValue);
                userInformation.setVoluntarily(this.getnextyear());
                LevelHistory levelHistory=new LevelHistory();
                levelHistory.setUserId(userId);
                levelHistory.setUserName(userInformation.getUserName());
                levelHistory.setRemarks("升级");
                levelHistoryDao.save(levelHistory);

                HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/sms/send/sendSms/itf", RequestMethod.post);
                httpRequest.addParameter("phones",userInformation.getPhoneNumber());
                httpRequest.addParameter("code",ConstantData.up);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username",userInformation.getUserName());
                jsonObject.put("vipType",vipRule.getVipName());
                httpRequest.addParameter("json",jsonObject);
                httpRequest.run();

            }else {
                userInformation.setGrowthValue(newGrowthValue);
            }
        }
        return userInformation;
        }


    @Transactional
    public String voluntarily(){
        Date date = new Date();//获取时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//转换格式
        String currentDate = sdf.format(date);
        List<UserInformation> userInformations = userInformationDao.findByVoluntarily(currentDate);//获取所有需要保级降级的人
        for (int i = 0; i <userInformations.size() ; i++) {
            UserInformation userInformation=userInformations.get(i);
           Integer levelId = userInformation.getVipRule().getId();//当前等级Id
           Integer growthValue = userInformation.getGrowthValue();//当前积分
            Integer minId=vipRuleDao.findMinId();//最小的id
            VipRule vipRule1= vipRuleDao.findOne(levelId);//当前等级规则
           Integer keep = vipRule1.getKeep();
            Integer require = vipRule1.getRequireValue();
           if(growthValue<keep){//未达到保持要求
               if(minId==levelId){//等级已经最小
                   userInformation.setGrowthValue(0);
                   userInformation.setVoluntarily(this.getnextyear());
                   userInformationDao.save(userInformation);
                   LevelHistory levelHistory=new LevelHistory();
                   levelHistory.setUserId(userInformation.getId());
                   levelHistory.setUserName(userInformation.getUserName());
                   levelHistory.setRemarks("降级");
                   levelHistoryDao.save(levelHistory);
               }else {//等级非最小
                   List<VipRule> list=vipRuleDao.findMin(require);//小于当前等级的所有集合   ASC
                   VipRule vipRule=list.get(list.size()-1);//获取下一级规则
                   userInformation.setVipRule(vipRule);
                   userInformation.setGrowthValue(vipRule.getRequireValue());
                   userInformation.setVoluntarily(this.getnextyear());
                   userInformationDao.save(userInformation);
                   LevelHistory levelHistory=new LevelHistory();
                   levelHistory.setUserId(userInformation.getId());
                   levelHistory.setUserName(userInformation.getUserName());
                   levelHistory.setRemarks("降级");
                   levelHistoryDao.save(levelHistory);
               }
           }else {//达到保持要求
               Integer newGrowthValue = growthValue-keep+require;
               userInformation.setVoluntarily(this.getnextyear());
               userInformation.setGrowthValue(newGrowthValue);
               userInformationDao.save(userInformation);
               LevelHistory levelHistory=new LevelHistory();
               levelHistory.setUserId(userInformation.getId());
               levelHistory.setUserName(userInformation.getUserName());
               levelHistory.setRemarks("保级");
               levelHistoryDao.save(levelHistory);
           }
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }

    @Override
    public String update(Integer userId, Integer levelId) throws YesmywineException {
        UserInformation userInformation = userInformationDao.findOne(userId);
        VipRule vipRule=vipRuleDao.findOne(levelId);
        userInformation.setVipRule(vipRule);
        userInformationDao.save(userInformation);
        return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
    }

    public String getnextyear() {//获取下一年
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
        Date date = curr.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//转换格式
        String sys = sdf.format(date);
        return sys;
    }
}
