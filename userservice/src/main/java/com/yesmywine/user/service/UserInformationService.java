
package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.util.error.YesmywineException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by light on 2016/12/13.
 */
public interface UserInformationService extends BaseService<UserInformation,Integer> {

    UserInformation findByUserId(Integer userId) throws YesmywineException;
    String initalize( Integer userId) throws YesmywineException;

    UserInformation findByUserName(String userName);
     String saveUserImg(Integer userId, Integer[] imgIds) throws YesmywineException;

    String update(Map<String,String> map,HttpServletRequest request)throws YesmywineException;

    String message(HttpServletRequest request,String phone)throws YesmywineException;

    String messageForget(HttpServletRequest request,String phone)throws YesmywineException;

    String bindingPhone(HttpServletRequest request,String message,String phone)throws YesmywineException;

    String resetPayPassword(HttpServletRequest request,String phone)throws YesmywineException;

    String updatePayPassword(HttpServletRequest request,String oldPassword,String payPasswordFirst)throws YesmywineException;

    String payPassword(HttpServletRequest request,String payPasswordFirst)throws YesmywineException;

    String localConsume(Map<String, String> params, Integer userId);

    String saveOrUpdate(String jsonData);

    String updateFirst(HttpServletRequest request,String message,String phone)throws YesmywineException;

    String updatePassword(String passwordFirst,String phone)throws YesmywineException;

    String updatePhone(HttpServletRequest request, String message,String phone)throws YesmywineException;

    String updateResgister(HttpServletRequest request,String message,String email)throws YesmywineException;
}

