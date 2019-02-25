package com.yesmywine.pay.dao;

import com.yesmywine.pay.entity.UnionPaySetting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SJQ on 2017/3/2.
 */
public interface UnionPaySettingDao extends JpaRepository<UnionPaySetting, Integer> {
}
