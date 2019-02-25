package com.yesmywine.sso.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.sso.bean.AdminUser;
import com.yesmywine.util.error.YesmywineException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by SJQ on 2017/2/9.
 */
public interface AdminUserService extends BaseService<AdminUser,Integer> {


    String updateByToken(String token, HttpServletRequest request) throws YesmywineException;

    AdminUser findByUsername(String loginName);

    Object getUserMenus(Integer userId);

    Object getUserPerms(Integer userId, Integer menuId);

    String configUserRoles(Integer userId, Integer[] rolesId) throws YesmywineException;

    Boolean verifyPermKey(String userInfo, String key)throws YesmywineException;

    void delete(Integer id);
}
