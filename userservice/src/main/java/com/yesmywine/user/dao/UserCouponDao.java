package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.UserCoupon;

import java.util.List;

/**
 * Created by ${shuang} on 2017/4/13.
 */
public interface UserCouponDao extends BaseRepository<UserCoupon, Integer> {

    List<UserCoupon> findByUserId(Integer userId);

    List<UserCoupon> findByUserIdAndCouponId(Integer userId,Integer couponId);


    UserCoupon findByUserIdAndCodeNumber(Integer userId, String codeNumber);
}
