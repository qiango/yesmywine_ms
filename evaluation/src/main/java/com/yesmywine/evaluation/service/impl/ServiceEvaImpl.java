package com.yesmywine.evaluation.service.impl;

import com.alibaba.fastjson.JSON;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.evaluation.bean.ServiceEva;
import com.yesmywine.evaluation.dao.ServiceDao;
import com.yesmywine.evaluation.service.ServiceEvaService;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by hz on 6/25/17.
 */
@Service
@Transactional
public class ServiceEvaImpl extends BaseServiceImpl<ServiceEva, Integer> implements ServiceEvaService {

    @Autowired
    private ServiceDao serviceDao;
    @Override
    public String create(Map<String ,String> para, HttpServletRequest request) throws YesmywineException {
        String userName= UserUtils.getUserName(request);
        ServiceEva serviceEva=new ServiceEva();
        serviceEva.setOrderNumber(para.get("orderNumber"));
        serviceEva.setServiceAttitude(Integer.parseInt(para.get("serviceAttitude")));
        serviceEva.setLogistics(Integer.parseInt(para.get("logistics")));
        serviceEva.setCommodityPackaging(Integer.parseInt(para.get("commodity")));
        serviceEva.setUserName(userName);
        serviceDao.save(serviceEva);
        String result= SynchronizeUtils.getResult(Dictionary.MALL_HOST,"/orders/orders/evaluate/itf", RequestMethod.post,"orderNo",para.get("orderNumber"));
        String code = JSON.parseObject(result).getString("code");
        if(!"201".equals(code)|| ValueUtil.isEmpity(code)){
            String msg=JSON.parseObject(result).getString("msg");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError(msg);
        }
        return "success";
    }
}
