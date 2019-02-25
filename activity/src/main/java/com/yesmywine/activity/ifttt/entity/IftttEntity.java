package com.yesmywine.activity.ifttt.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "ifttt")
public class IftttEntity extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(50) COMMENT '编码'")
    private String code;
    @Column(columnDefinition = "varchar(20) COMMENT '名称'")
    private String name;
    @Column(columnDefinition = "varchar(150) COMMENT '包路径'")
    private String packagePath;

    @Enumerated(EnumType.STRING)
    private IftttEnum type;//类型  trigger触发类   action执行类


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public IftttEnum getType() {
        return type;
    }

    public void setType(IftttEnum type) {
        this.type = type;
    }
}
