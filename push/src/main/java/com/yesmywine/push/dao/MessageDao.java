package com.yesmywine.push.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.push.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 12/12/16.
 */
@Repository
public interface MessageDao extends BaseRepository<Message, Integer> {

    List<Message> findByStatus(Integer status);

}
