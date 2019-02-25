package com.yesmywine.goods.service.Impl;

import com.yesmywine.goods.bean.BrandEnum;
import com.yesmywine.goods.entityProperties.Brand;
import com.yesmywine.goods.dao.BrandDao;
import com.yesmywine.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hz on 12/12/16.
 */
@Service
public class BrandSrviceImpl implements BrandService {
    @Autowired
    private BrandDao brandRepoitory;

    public Integer find(String brandCnName) { //插入返回ｉｄ
        Integer id = null;
        List<Brand> brandCnNameList = brandRepoitory.findByBrandCnNameAndBrandEnum(brandCnName, BrandEnum.available);
        if (null == brandCnNameList || brandCnNameList.size() < 1) {
            id = insert(brandCnName);
        } else {
            id = brandCnNameList.get(0).getId();
        }
        return id;
    }


    public Integer insert(String brandCnName) {   //插入
        if (null == brandRepoitory.findIdByBrandCnName(brandCnName)) {
            Brand brand = new Brand(brandCnName);
            brandRepoitory.save(brand);
            return brand.getId();
        }
        return null;
    }


}
