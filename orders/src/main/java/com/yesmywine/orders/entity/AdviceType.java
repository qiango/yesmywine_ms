package com.yesmywine.orders.entity;

import javax.persistence.*;

/**
 * 咨询分类
 * Created by light on 2017/1/6.
 */
@Entity
@Table(name = "adviceType")
public class AdviceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(50) COMMENT '类型名称'")
    private String name;//类型名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
