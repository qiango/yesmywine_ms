package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.Label;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 12/12/16.
 */
@Repository
public interface LabelDao extends BaseRepository<Label, Integer> {

    Label findByName(String name);

    Label findByParentName(Label label);

}
