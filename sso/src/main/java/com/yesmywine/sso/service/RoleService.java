package com.yesmywine.sso.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.sso.bean.Perms;
import com.yesmywine.sso.bean.Roles;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;

/**
 * Created by SJQ on 2017/6/6.
 */
public interface RoleService extends BaseService<Roles,Integer> {
    Roles create(String roleName, Integer []psermsIds) throws YesmywineException;

    Roles update(Integer id, String roleName, Integer[] permIds) throws YesmywineException;

    String delete(Integer id) throws YesmywineException;
    String quanxian(List<Perms> list) throws YesmywineException;
}
