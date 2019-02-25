package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.WineCellarFlow;

import java.util.List;

/**
 * Created by ${shuang} on 2017/6/20.
 */
public interface WineCellarFlowDao  extends BaseRepository<WineCellarFlow, Integer> {
    List<WineCellarFlow> findByUserIdAndOrderNumber(Integer integer, String orderNumber);
}
