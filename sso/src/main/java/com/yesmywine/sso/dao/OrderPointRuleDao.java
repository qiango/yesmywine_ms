package com.yesmywine.sso.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.sso.bean.ChargePointRule;
import com.yesmywine.sso.bean.OrderPointRule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by ${shuang} on 2017/7/11.
 */
public interface OrderPointRuleDao extends BaseRepository<OrderPointRule,Integer> {

    @Modifying
    @Query("UPDATE OrderPointRule SET status = 0 where  status = 1")
    void initialization();

    OrderPointRule findByStatus(int i);

    OrderPointRule findByMultiple(Integer s);
}
