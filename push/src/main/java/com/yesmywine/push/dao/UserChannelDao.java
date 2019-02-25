package com.yesmywine.push.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.push.entity.Message;
import com.yesmywine.push.entity.UserChannel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 12/12/16.
 */
@Repository
public interface UserChannelDao extends BaseRepository<UserChannel, Integer> {

    List<UserChannel> findByUserId(String userId);

    UserChannel findByUserIdAndChannelId(String userId, String channelId);

}
