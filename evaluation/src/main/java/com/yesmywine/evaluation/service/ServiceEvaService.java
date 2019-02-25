package com.yesmywine.evaluation.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.evaluation.bean.ServiceEva;
import com.yesmywine.util.error.YesmywineException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by hz on 6/25/17.
 */
public interface ServiceEvaService extends BaseService<ServiceEva,Integer> {

   String create(Map<String ,String> para,HttpServletRequest request)throws YesmywineException;

}
