package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.MenuSecent;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface MenuSecentDao extends BaseRepository<MenuSecent,Integer> {

    List<MenuSecent> findBySecentCategoryId(Integer secentCategoryId);

    MenuSecent findBySecentIndexAndFirstMenuId(Integer secentIndex, Integer firstMenuId);

    List<MenuSecent> findByFirstMenuIdOrderBySecentIndex(Integer firstMenuId);

    void deleteByFirstMenuId(Integer firstMenuId);
}
