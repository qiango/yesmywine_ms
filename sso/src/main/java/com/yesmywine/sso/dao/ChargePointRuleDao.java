package com.yesmywine.sso.dao;


import com.yesmywine.sso.bean.ChargePointRule;
import com.yesmywine.base.record.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by ${shuang} on 2017/7/10.
 */

public interface ChargePointRuleDao extends BaseRepository<ChargePointRule,Integer> {

    @Modifying
    @Query("UPDATE ChargePointRule SET status = 0 where  status = 1")
   void initialization();

    ChargePointRule findByStatus(int i);

    ChargePointRule findByMultiple(Integer s);
}
