package com.yesmywine.user.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.entity.VipRule;
import com.yesmywine.user.service.UserInformationService;
import com.yesmywine.user.service.UserLevelService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by ${shuang} on 2016/12/22.
 */
@RestController
@RequestMapping("/userservice/viplevel")
public class VipController {

    @Autowired
    private UserLevelService userLevelService;
    @Autowired
    private  UserInformationService userInformationService;
    @Autowired
    private UserInformationDao userInformationDao;


    @RequestMapping(value = "/task",method = RequestMethod.GET)
    public String keep() {//保级降级
        try {
            System.out.println("=============================每日凌晨计算用户等级 开始================================");
            userLevelService.voluntarily();
            System.out.println("=============================每日凌晨计算用户等级 结束================================");
            return ValueUtil.toJson("每日凌晨计算用户等级    "+ DateUtil.getNowTime());
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());

            return ValueUtil.toError(e.getCode(),"每日凌晨计算用户等级    "+ DateUtil.getNowTime()+"  " +e.getMessage());
        }
    }

    //    @Description   查询列表
    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, String> params, Integer pageNo, Integer pageSize,Integer userId,Integer levelId) {
        if(ValueUtil.isEmpity(levelId)&&ValueUtil.isEmpity(userId)){
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
            if(userId==null) {
                params.remove(params.remove("userId").toString());
            }
            if(levelId==null) {
                params.remove(params.remove("levelId").toString());
            }
            pageModel.addCondition(params);
            pageModel = userInformationService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
        }else {
            VipRule vipRule = new VipRule();
            vipRule.setId(levelId);
            UserInformation userInformation = userInformationDao.findByIdAndVipRule(userId,vipRule);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformation);
        }
    }

}
