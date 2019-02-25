
package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.ChargeFlow;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/4/5.
 */
public interface ChargeService extends BaseService<ChargeFlow,Integer> {

    Object charge(@RequestParam Map<String, String> params) throws YesmywineException;//充值

    Object consume(Map<String, String> params, Integer userId);

    Object chargeFlow(Map<String, String> params, Integer userId);
}
