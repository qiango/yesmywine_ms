package com.yesmywine.sso.service.serviceImpl;


import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.sso.bean.ChargePointRule;
import com.yesmywine.sso.dao.ChargePointRuleDao;
import com.yesmywine.sso.service.chargePointRuleService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * Created by ${shuang} on 2017/7/10.
 */
@Service
public class chargePointRuleServiceImpl extends BaseServiceImpl<ChargePointRule,Integer> implements chargePointRuleService {
  @Autowired
  private ChargePointRuleDao chargePointRuleDao;


//  设置充值兑换积分规则
    @Override
    public String allocation(String rule) throws YesmywineException {
        String [] array =rule.split(",");
        for (int i = 0; i <array.length ; i++) {
            ChargePointRule old =chargePointRuleDao.findByMultiple(Integer.valueOf(array[i]));
            if(ValueUtil.isEmpity(old)){
                ChargePointRule chargePointRule = new ChargePointRule();
                chargePointRule.setMultiple(Integer.valueOf(array[i]));
                chargePointRuleDao.save(chargePointRule);
            }
        }
        return "success";
    }
//启用规则
    @Override
    public String setRule(Integer id) throws YesmywineException {
        chargePointRuleDao.initialization();
        ChargePointRule chargePointRule =chargePointRuleDao.findOne(id);
        chargePointRule.setStatus(1);
        chargePointRuleDao.save(chargePointRule);
        return "success";
    }

//    充值返还积分
    @Override
    public String getPoint(Double money) throws YesmywineException {
        BigDecimal bigDecimal = new BigDecimal(money);
        ChargePointRule chargePointRule =chargePointRuleDao.findByStatus(1);
        BigDecimal bigDecimal2 = new BigDecimal(chargePointRule.getMultiple());
        int point = (int) bigDecimal.multiply(bigDecimal2).setScale(0, ROUND_HALF_DOWN).doubleValue();
        return String.valueOf(point);
    }
}
