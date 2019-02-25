package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.UserEmail;
import org.springframework.stereotype.Repository;

/**
 * Created by hz on 7/14/17.
 */
@Repository
public interface UserEmailDao extends BaseRepository<UserEmail,Integer> {

    UserEmail findByUserId(Integer userId);
    UserEmail findByEmail(String email);

}
