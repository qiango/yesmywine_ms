package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.StoreWineflowAttach;

import java.util.List;

/**
 * Created by ${shuang} on 2017/8/11.
 */
public interface StoreWineflowAttachDao extends BaseRepository<StoreWineflowAttach, Integer> {

    List<StoreWineflowAttach> findByExtractorderNumber(String extractorderNumber);
}
