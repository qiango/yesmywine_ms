package com.yesmywine.user.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.user.dao.UserBlackDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.UserBlack;
import com.yesmywine.user.service.UserBlackService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by light on 2017/2/10.
 */
@Service
public class UserBlackServiceImpl extends BaseServiceImpl<UserBlack, Integer> implements UserBlackService {

    @Autowired
    private UserBlackDao userBlackDao;
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private UserBlackService userBlackService;

    @Override
    public UserBlack findByUserId(Integer userId) {
        return userBlackDao.findByUserId(userId);
    }

    @Override
    public String disable(Integer userId, String reason) throws YesmywineException {
        try {
            ValueUtil.verify(userId);
            ValueUtil.verify(reason);
            UserBlack isBlackUser = userBlackService.findByUserId(userId);
            if(isBlackUser!=null){//判断是否黑名单用户有数据
                if (isBlackUser.getBlackStatus()==1) {
                    isBlackUser.setBlackStatus(2);
                    isBlackUser.setReason(reason);
                    isBlackUser.setCreateTime(new Date());
                    userBlackService.save(isBlackUser);
                    return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
                }else {
                    return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"该用户已经是黑名单用户","success");
                }
            }else{
                UserBlack userBlack=new UserBlack();
                String userName=userInformationDao.findOne(userId).getUserName();
                userBlack.setUserName(userName);
                userBlack.setUserId(userId);
                userBlack.setReason(reason);
                userBlackService.save(userBlack);
                return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
            }
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @Override
    public String update(Integer userId, String reason) throws YesmywineException {
//        更新黑名单用户
        try {
            ValueUtil.verify(userId);
            ValueUtil.verify(reason);
            UserBlack blackUser = userBlackService.findByUserId(userId);
            if(ValueUtil.isEmpity(blackUser)){//不存在
                return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"该用户不在黑名单中");
            }else {
                if(blackUser.getBlackStatus()==1){
                    return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"该用户黑名单状态已解除");
                }else {
                    blackUser.setReason(reason);
                    userBlackService.save(blackUser);
                    return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
                }
            }
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @Override
    public String recover(String userIdList) throws YesmywineException {
//        恢复黑名单
        try {
            ValueUtil.verify(userIdList);
        } catch (YesmywineException e) {
            return ValueUtil.toJson(e.getCode(), e.getMessage());
        }
        String [] userIds= userIdList.split(",");
//        支持批量回复
        for (int i = 0; i <userIds.length ; i++) {
            Integer userId=Integer.valueOf(userIds[i]);
            UserBlack blackUser = userBlackService.findByUserId(userId);
            if(ValueUtil.notEmpity(blackUser)){
                if (blackUser.getBlackStatus()==2) {
                    blackUser.setBlackStatus(1);
                    userBlackService.save(blackUser);
                }
            }
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
    }

    @Override
    public String isBlack(Integer userId) throws YesmywineException {
//        判断是否是黑名单
        try {
            ValueUtil.verify(userId);
            UserBlack user = userBlackService.findByUserId(userId);
            Map<String, Object> map = new HashMap<String, Object>();
            if(ValueUtil.isEmpity(user)){
                map.put("isBlack", false);
            }else if(user.getBlackStatus()==1){
                map.put("isBlack", false);
            } else if(ValueUtil.notEmpity(user)||user.getBlackStatus()==2) {
                map.put("isBlack", true);
            }
            return ValueUtil.toJson(HttpStatus.SC_OK,map);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }


}
