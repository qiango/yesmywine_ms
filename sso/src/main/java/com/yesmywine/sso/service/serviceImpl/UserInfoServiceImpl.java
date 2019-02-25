package com.yesmywine.sso.service.serviceImpl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.sso.bean.AdminUser;
import com.yesmywine.sso.bean.UserInformation;
import com.yesmywine.sso.dao.AdminUserDao;
import com.yesmywine.sso.dao.UserInfoDao;
import com.yesmywine.sso.service.UserInfoService;
import com.yesmywine.sso.utils.PasswordUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by SJQ on 2017/6/18.
 */
@Service
@Transactional
public class UserInfoServiceImpl extends BaseServiceImpl<UserInformation,Integer> implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private AdminUserDao adminUserDao;

    @Override
    public UserInformation findByUsername(String loginName) {
        return userInfoDao.findByUserName(loginName);
    }

//    用户注册
    @Override
    public UserInformation register(String userName, String password, String registerChannel,String phoneVerifyCode) throws YesmywineException {
        AdminUser adminUser = adminUserDao.findByUserName(userName);
        if(adminUser!=null){
            ValueUtil.isError("该用户已存在");
        }
        UserInformation userInformation = userInfoDao.findByUserName(userName);
        if(userInformation!=null){
            ValueUtil.isError("该用户已存在");
        }
//        用户初始化
        userInformation = new UserInformation();
        userInformation.setPhoneNumber(userName);
        userInformation.setBindPhoneFlag(true);
        userInformation.setUserName(userName);
        userInformation.setPassword(PasswordUtils.encodePassword(password));
//        酒豆初始化
        userInformation.setBean(0.0);
//        成长值初始化
        userInformation.setGrowthValue(0);
        userInformation.setBindEmailFlag(false);
        userInformation.setChannelType(1);
        userInformation.setNickName(userName);
//        余额初始化
        userInformation.setRemainingSum(0.0);
        userInformation.setRegisterChannel("GW");
        userInfoDao.save(userInformation);

        return userInformation;
    }

    //用户删除
    @Override
    public void delete(UserInformation userInformation) {
        userInfoDao.delete(userInformation);
    }

    @Override
    public UserInformation findById(Integer id) {
        return userInfoDao.findOne(id);
    }
}
