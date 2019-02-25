package com.yesmywine.evaluation.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.evaluation.bean.ServiceEva;
import org.springframework.stereotype.Repository;

/**
 * Created by hz on 6/25/17.
 */
@Repository
public interface ServiceDao extends BaseRepository<ServiceEva,Integer> {
}
