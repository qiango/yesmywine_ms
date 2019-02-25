package com.yesmywine.orders.dao;


import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.FreightAttach;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by ${shuang} on 2017/7/27.
 */
@Repository
public interface FreightAttachDao extends BaseRepository<FreightAttach, Integer> {
    @Modifying
    @Query("DELETE FROM FreightAttach WHERE freightId=:freightId")
    void deleteByFreightId(@Param("freightId")Integer freightId);

    FreightAttach findByAreaId(String areaId);

    FreightAttach findByAreaIdOrName(String areaId,String name);

    FreightAttach findByName(String name);
}
