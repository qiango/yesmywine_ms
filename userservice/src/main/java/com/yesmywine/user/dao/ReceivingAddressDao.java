package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.ReceivingAddress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/3.
 */
@Repository
public interface ReceivingAddressDao extends BaseRepository<ReceivingAddress, Integer> {
    List<ReceivingAddress> findByUserId(Integer userId);
    List<ReceivingAddress> findByUserIdAndIdNot(Integer userId,Integer id);
    ReceivingAddress findByUserIdAndStatus(Integer userId,Integer status);

    @Query("select count(*) from ReceivingAddress where userId=:userId")
    Integer countUserId(@Param("userId") Integer userId);
}
