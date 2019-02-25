package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.StoreWineAttach;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by ${shuang} on 2017/8/10.
 */
public interface StoreWineAttachDao   extends BaseRepository<StoreWineAttach, Integer> {

    StoreWineAttach findByOrderNumberAndGoodsId(String orderNumber, Integer goodsId);

    @Query("from StoreWineAttach where orderNumber=:orderNumber and extractable>0 ")
    List<StoreWineAttach> findExtractable(@Param("orderNumber")String orderNumber);


    @Query("select sum(extractable)  from StoreWineAttach where orderNumber=:orderNumber ")
    int sumByOrderNumber(@Param("orderNumber")String orderNumber);


}
