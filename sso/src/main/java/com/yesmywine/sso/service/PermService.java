package com.yesmywine.sso.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.sso.bean.Perms;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by SJQ on 2017/6/9.
 */
public interface PermService extends BaseService<Perms,Integer> {
    Perms create(Perms perms) throws YesmywineException;

    Perms update(Perms perms) throws YesmywineException;

    String delete(Integer id) throws YesmywineException;


}
