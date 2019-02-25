package com.yesmywine.goods.util;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by WANG, RUIQING on 1/10/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
public class TestRedisBiz {


    @Cacheable(value = "User", key = "'UserId_' + #id", condition = "#id<=110")
    public String queryFullNameById(long id) {
        System.out.println("execute queryFullNameById method");
        return "ZhangSanFeng";
    }

    @CacheEvict(value = "User", key = "'UserId_' + #id")
    public void deleteById(long id) {
        System.out.println("execute deleteById method");
    }

    @CachePut(value = "User", key = "'UserId_' + #id")
    public String modifyFullNameById(long id, String newName) {
        System.out.println("execute modifyFullNameById method");
        return newName;
    }
}
