package com.yesmywine.pay.dao;

import com.yesmywine.pay.entity.AlipaySetting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SJQ on 2017/3/2.
 */
public interface AlipaySettingDao extends JpaRepository<AlipaySetting, Integer> {
}
