package com.yesmywine.sso.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.sso.bean.Perms;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by SJQ on 2017/6/6.
 */
public interface PermDao extends BaseRepository<Perms,Integer> {
    @Query(value ="select p.* from perms p where p.isMenu='true' and p.level=:level and p.id in (select distinct rp.pid from rolePerm rp where rp.rid in (select ur.rid from userRole ur where uid=:userId)) ",nativeQuery=true)
    List<Perms> findUserMenu( @Param("userId") Integer userId,@Param("level")String level);

    List<Perms> findByParentId(Integer id);
    List<Perms> findByLevel(Integer id);

    List<Perms> findByParentIdAndIsMenu(Integer id, String isMenu);
    @Query(value ="select p.* from perms p where p.isMenu='false' and p.parentId=:menuId and p.id in (select distinct rp.pid from rolePerm rp where rp.rid in (select ur.rid from userRole ur where uid=:userId)) ",nativeQuery=true)
    List<Perms> findUserMenuFunction(@Param("userId")Integer userId, @Param("menuId") Integer menuId);

    Perms findByPermKey(String permKey);

    Perms findByPermName(String permName);

    @Query(value = "select rp.rid from rolePerm rp where rp.pid=:permId ",nativeQuery = true)
    List<Integer> findByPermId(@Param("permId") Integer permId);

}
