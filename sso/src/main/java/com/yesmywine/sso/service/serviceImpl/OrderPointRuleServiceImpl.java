package com.yesmywine.sso.service.serviceImpl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.sso.bean.OrderPointRule;
import com.yesmywine.sso.dao.OrderPointRuleDao;
import com.yesmywine.sso.service.OrderPointRuleService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * Created by ${shuang} on 2017/7/11.
 */
//下单送积分转换服务
@Service
public class OrderPointRuleServiceImpl extends BaseServiceImpl<OrderPointRule,Integer> implements OrderPointRuleService {

    @Autowired
    private OrderPointRuleDao orderPointRuleDao;
    //设置规则
    @Override
    public String allocation(String rule) throws YesmywineException {
        String [] array =rule.split(",");
        for (int i = 0; i <array.length ; i++) {
            OrderPointRule old =orderPointRuleDao.findByMultiple(Integer.valueOf(array[i]));
            if(ValueUtil.isEmpity(old)){
                OrderPointRule orderPointRule = new OrderPointRule();
                orderPointRule.setMultiple(Integer.valueOf(array[i]));
                orderPointRuleDao.save(orderPointRule);
            }
        }
        return "success";
    }

    @Override
    public String setRule(Integer id) throws YesmywineException {
//        初始化数据库数据
        orderPointRuleDao.initialization();
        //启用规则
        OrderPointRule orderPointRule =orderPointRuleDao.findOne(id);
        orderPointRule.setStatus(1);
        orderPointRuleDao.save(orderPointRule);
        return "success";
    }

    @Override
    public String getPoint(Double money) throws YesmywineException {
//        下单金额获得积分
        BigDecimal bigDecimal = new BigDecimal(money);
        OrderPointRule orderPointRule =orderPointRuleDao.findByStatus(1);
        BigDecimal bigDecimal2 = new BigDecimal(orderPointRule.getMultiple());
        int point = (int) bigDecimal.multiply(bigDecimal2).setScale(0, ROUND_HALF_DOWN).doubleValue();

        return String.valueOf(point);
    }
}
