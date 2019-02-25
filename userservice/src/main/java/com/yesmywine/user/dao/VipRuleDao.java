package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.VipRule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ${shuang} on 2017/4/10.
 */
@Repository
public interface VipRuleDao extends BaseRepository<VipRule, Integer> {

    @Query("FROM VipRule WHERE requireValue>:require ORDER BY requireValue ASC ")
    List<VipRule> findMax(@Param("require")Integer requireValue);


//    @Query("select Min()from VipRule where requie")
//    VipRule findminVipRule()

    @Query("select id from VipRule where  requireValue=(select min (requireValue) from VipRule)")
    Integer findMinId();

    @Query("FROM VipRule WHERE requireValue<:requireValue ORDER BY requireValue ASC")
    List<VipRule> findMin(@Param("requireValue")Integer requireValue);

    VipRule findByVipName(String name);
}
