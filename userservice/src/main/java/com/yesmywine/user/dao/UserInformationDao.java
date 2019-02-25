package com.yesmywine.user.dao;


import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.entity.VipRule;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by light on 2016/12/19.
 */
@Repository
public interface UserInformationDao extends BaseRepository<UserInformation, Integer> {



    public List<UserInformation> findAll();

    List<UserInformation> findByVoluntarily(String currentDate);

    UserInformation findByIdAndVipRule(Integer userId, VipRule vipRule);

    List<UserInformation> findByVipRule(VipRule vipRule);

    UserInformation findByUserName(String userName);

    UserInformation findByUserNameOrPhoneNumber(String userName, String phoneNumber);

    UserInformation findByEmailAndBindEmailFlag(String email,Boolean flag);

    UserInformation findByEmailAndCodeEmail(String email,String flag);

    UserInformation findByPhoneNumber(String phone);

    UserInformation findByNickName(String nickName);

//    List<UserInformation> findByLevelId(Integer id);
//
//    UserInformation findByIdAndLevelId(Integer userId,Integer levelId);
}
