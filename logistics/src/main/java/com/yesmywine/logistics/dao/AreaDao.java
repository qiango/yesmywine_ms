package com.yesmywine.logistics.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.logistics.entity.Area;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangdiandian on 2017/4/13.
 */
@Repository
public interface AreaDao extends BaseRepository<Area,Integer> {
    @Query(value = "select * from area where parentId = :parentId",nativeQuery = true)
    List<Area> findByParentId(@Param("parentId") Integer parentId);
    List<Area> findByParentName(Area area);
    Area findByCityName(String cityName);
}
