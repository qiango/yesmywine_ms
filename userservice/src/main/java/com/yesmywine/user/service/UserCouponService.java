package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.UserCoupon;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by ${shuang} on 2017/4/19.
 */
public interface UserCouponService extends BaseService<UserCoupon,Integer> {
    Boolean draw(Integer userId,Integer couponId) throws YesmywineException;//领取
    String selfList(Integer userId) throws YesmywineException;//个人详情列表
    String use(Integer userId,Integer userCouponId) throws YesmywineException;//使用
    String ListOnBalance(Integer userId, String json)  throws YesmywineException;//结算时可用列表

    String returns(Integer userId, Integer id) throws YesmywineException;
}
