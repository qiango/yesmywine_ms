package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.AppAdvertising;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 7/5/17.
 */
@Repository
public interface AppAdvertisingDao extends BaseRepository<AppAdvertising,Integer> {

    List<AppAdvertising> findByTopIdOrCollaborateIdOrImportantId(Integer topId
    , Integer collaborateId, Integer importantId);

}
