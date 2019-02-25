package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.BeanFlow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ${shuang} on 2017/4/12.
 */
@Repository
public interface BeanFlowDao extends BaseRepository<BeanFlow,Integer> {
    BeanFlow findByUserIdAndOrderNumber(Integer userId, String orderNumber);


    Page<BeanFlow> findByStatusIn(List status, Pageable pageable);
}
