package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.Coupon;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ${shuang} on 2017/4/13.
 */
public interface CouponDao extends BaseRepository<Coupon, Integer> {
    @Query("FROM Coupon WHERE status!=2 AND status!=3")
    List<Coupon> findSome();

    List<Coupon> findByAuditStatus(Integer auditStatus);
}
