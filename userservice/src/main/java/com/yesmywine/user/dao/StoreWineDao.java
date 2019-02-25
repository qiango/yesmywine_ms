package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.StoreWine;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ${shuang} on 2017/8/10.
 */
public interface StoreWineDao extends BaseRepository<StoreWine, Integer> {

    StoreWine findByUserIdAndId(Integer userId, int i);


    @Modifying
    @Query("update StoreWine SET isOver=1 WHERE orderNumber=:orderNumber")
    void findByOrderNumbersetIsover(@Param("orderNumber") String orderNumber);

    StoreWine findByOrderNumber(String orderNumber);
}
