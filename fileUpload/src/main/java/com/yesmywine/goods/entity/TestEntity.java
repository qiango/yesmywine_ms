package com.yesmywine.goods.entity;

import com.yesmywine.base.record.bean.VerifyType;
import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by WANG, RUIQING on 12/19/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "testentity")
public class TestEntity extends BaseEntity<Integer> {

    private String name;
    private String age;
    protected Date syncTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    {
        this.addVerify(VerifyType.notNull, "name");
        this.addVerify(VerifyType.email, "age");
    }

    @Override
    public void afterFind() {
        super.afterFind();
        this.syncTime = null;
    }

}
