package com.yesmywine.sso.service.serviceImpl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.sso.bean.ChargePointRule;
import com.yesmywine.sso.bean.OrderPointRule;
import com.yesmywine.sso.bean.WebDescribe;
import com.yesmywine.sso.dao.ChargePointRuleDao;
import com.yesmywine.sso.dao.OrderPointRuleDao;
import com.yesmywine.sso.dao.WebDescribeDao;
import com.yesmywine.sso.service.WebDescribeService;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/6/15.
 */
@Service
public class WebDescribeServiceImpl extends BaseServiceImpl<WebDescribe,Integer> implements WebDescribeService {


    @Autowired
    private WebDescribeDao webDescribeDao;
    @Autowired
    private OrderPointRuleDao orderPointRuleDao;
    @Autowired
    private ChargePointRuleDao chargePointRuleDao;

//    添加网站说明
    @Override
    public String insert(Map<String, String> params) throws YesmywineException {
        WebDescribe webDescribe = new WebDescribe();
        webDescribe.setWebDescribe(params.get("webDescribe"));
        webDescribe.setKeyword(params.get("keyword"));
        webDescribe.setWebTitle(params.get("webTitle"));
        webDescribe.setDays(Integer.valueOf(params.get("days")));
        webDescribe.setPoints(Integer.valueOf(params.get("points")));
        webDescribeDao.save(webDescribe);
        OrderPointRule orderPointRule = new OrderPointRule();
        orderPointRule.setMultiple(Integer.valueOf(params.get("orderPoint")));
//        删除原来的设置然后重新植入新的数据，下同
        orderPointRuleDao.deleteAll();
        orderPointRuleDao.save(orderPointRule);
        ChargePointRule chargePointRule = new ChargePointRule();
        chargePointRule.setMultiple(Integer.valueOf(params.get("chargePoint")));
        chargePointRuleDao.deleteAll();
        chargePointRuleDao.save(chargePointRule);

        return "success";
    }

//    更新网站设置
    @Override
    public String update(Map<String, String> params) throws YesmywineException {
//        先查询到一个网页详情
        WebDescribe webDescribe = webDescribeDao.findOne(Integer.valueOf(params.get("id")));
        webDescribe.setWebDescribe(params.get("describe"));
        webDescribe.setKeyword(params.get("keyword"));
        webDescribe.setWebTitle(params.get("webTitle"));
        webDescribeDao.save(webDescribe);
        return "success";
    }
}
