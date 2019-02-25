package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.LevelHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ${shuang} on 2016/12/20.
 */
@Repository
public interface LevelHistoryDao extends BaseRepository<LevelHistory, Integer> {
    @Query("select Max(id) from LevelHistory where userId=:userId")
    Integer findmaxId(@Param("userId") Integer userId);

    List<LevelHistory> findByUserId(Integer userId);
}
