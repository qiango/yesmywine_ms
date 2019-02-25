package com.yesmywine.goods.dao;

import com.yesmywine.goods.bean.BrandEnum;
import com.yesmywine.goods.entityProperties.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 12/12/16.
 */
@Repository
public interface BrandDao extends JpaRepository<Brand, Integer> {
    @Query("select id from Brand where brandCnName = :name  and   brandEnum = 1 ")
    Integer findIdByBrandCnName(@Param("name") String name);

    List<Brand> findByBrandCnNameAndBrandEnum(String brandCnName, BrandEnum brandEnum);

}
