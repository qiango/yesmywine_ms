package com.yesmywine.user.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.dao.VipRuleDao;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.entity.VipRule;
import com.yesmywine.user.service.VipRuleService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ${shuang} on 2017/4/10.
 */
@Service
public class VipRuleServiceImpl extends BaseServiceImpl<VipRule, Integer> implements VipRuleService {

    @Autowired
    private VipRuleDao vipRuleDao;
    @Autowired
    private UserInformationDao userInformationDao;

    @Override
    public String insert(Map<String, String> params) throws YesmywineException {//新建规则
        ValueUtil.verify(params.get("vipName"));
        ValueUtil.verify(params.get("requireValue"));
        ValueUtil.verify(params.get("keep"));
        ValueUtil.verify(params.get("url"));
        ValueUtil.verify(params.get("keepDays"));
        ValueUtil.verify(params.get("discount"));
        if (ValueUtil.notEmpity(params.get("id"))) {
            VipRule newvipRule = vipRuleDao.findByVipName(params.get("vipName"));
            Integer id = Integer.valueOf(params.get("id"));
            if (newvipRule.getId()!=id) {
                return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR, "等级名称重复", "erro");
            }
            VipRule vipRule = vipRuleDao.findOne(Integer.valueOf(params.get("id")));
            vipRule.setDiscount(Double.valueOf(params.get("discount")));
            vipRule.setKeep(Integer.parseInt(params.get("keep")));
            vipRule.setKeepDays(Integer.parseInt(params.get("keepDays")));
            vipRule.setRequireValue(Integer.parseInt(params.get("requireValue")));
            vipRule.setVipName(params.get("vipName"));
            vipRule.setUrl(params.get("url"));
            vipRuleDao.save(vipRule);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
        } else {
            VipRule newvipRule = vipRuleDao.findByVipName(params.get("vipName"));
            if(ValueUtil.notEmpity(newvipRule)){
                return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR, "等级名称重复", "erro");
            }
            Boolean flag = this.judge(Integer.parseInt(params.get("requireValue")), Integer.parseInt(params.get("keep")));
            if (flag == false) {
                return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR, "范围冲突", "erro");
            }
            VipRule vipRule = new VipRule();
            vipRule.setDiscount(Double.valueOf(params.get("discount")));
            vipRule.setKeep(Integer.parseInt(params.get("keep")));
            vipRule.setKeepDays(Integer.parseInt(params.get("keepDays")));
            vipRule.setRequireValue(Integer.parseInt(params.get("requireValue")));
            vipRule.setVipName(params.get("vipName"));
            vipRule.setUrl(params.get("url"));
            VipRule vipRule1 = vipRuleDao.save(vipRule);
            String usercode = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,
                    "/user/vipRule/syn",
                    ValueUtil.toJson(HttpStatus.SC_CREATED, "save", vipRule1), com.yesmywine.httpclient.bean.RequestMethod.post);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, "success", "success");
        }

    }

    @Override
    public String delete(Map<String, String> params) throws YesmywineException {
        ValueUtil.verify(params.get("idList"));
        String id = params.get("idList");
        Integer minId = vipRuleDao.findMinId();//最小的id
        VipRule vp = new VipRule();
        vp.setId(Integer.valueOf(id));
        List<UserInformation> userList = userInformationDao.findByVipRule(vp);
        if (Integer.valueOf(id) == minId) {
            for (int j = 0; j < userList.size(); j++) {
                UserInformation userInformation = userList.get(j);
                VipRule vipRule1 = vipRuleDao.findOne(minId);//当前等级规则
                Integer require = vipRule1.getRequireValue();
                List<VipRule> list = vipRuleDao.findMax(require);//大于当前等级的所有集合   ASC升
                VipRule vipRule = list.get(0);//获取下一级规则
                userInformation.setVipRule(vipRule);
                userInformationDao.save(userInformation);
            }
        } else {
            for (int j = 0; j < userList.size(); j++) {
                UserInformation userInformation = userList.get(j);
                Integer levelId = userInformation.getVipRule().getId();//当前等级Id
                VipRule vipRule1 = vipRuleDao.findOne(levelId);//当前等级规则
                Integer require = vipRule1.getRequireValue();
                List<VipRule> list = vipRuleDao.findMin(require);//小于当前等级的所有集合   ASC
                VipRule vipRule = list.get(list.size() - 1);//获取下一级规则
                userInformation.setVipRule(vipRule);
                userInformationDao.save(userInformation);
            }
        }
        System.out.println("====================================");
        VipRule vipRule1 = vipRuleDao.findOne(Integer.valueOf(id));
        vipRuleDao.delete(Integer.valueOf(id));
        String usercode = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,
                "/user/vipRule/syn",
                ValueUtil.toJson(HttpStatus.SC_CREATED, "delete", vipRule1), com.yesmywine.httpclient.bean.RequestMethod.post);
        return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, "success");
    }


    public Boolean judge(Integer require, Integer keep) {
        Boolean flag = true;
        List<VipRule> vipRules = vipRuleDao.findAll();
        for (int i = 0; i < vipRules.size(); i++) {
            Integer oldRequire = vipRules.get(i).getRequireValue();
            Integer oldKeep = vipRules.get(i).getKeep();
            Range range = new Range(oldRequire, oldKeep);
            if (range.contains(require) || range.contains(keep) || (require < oldRequire && keep > oldKeep)) {//不能创建
                flag = false;
                break;
            }
        }
        return flag;
    }
}
