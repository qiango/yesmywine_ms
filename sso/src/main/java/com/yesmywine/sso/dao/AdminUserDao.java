package com.yesmywine.sso.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.sso.bean.AdminUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by SJQ on 2016/12/19.
 */
@Repository
public interface AdminUserDao extends BaseRepository<AdminUser, Integer> {

    List<AdminUser> queryUserInformationByEmail(String param);

    List<AdminUser> queryUserInformationByPhoneNumber(String param);

    AdminUser findByUserName(String loginName);

    @Query(value = "select ur.uid from userRole ur where ur.rid=:roleId ",nativeQuery = true)
    List<Integer> findByRoleId(@Param("roleId") Integer roleId);
}
