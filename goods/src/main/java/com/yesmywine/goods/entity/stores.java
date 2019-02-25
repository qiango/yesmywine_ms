package com.yesmywine.goods.entity;

import javax.persistence.*;

/**
 * Created by hz on 12/8/16.
 */
@Entity
@Table(name = "stores")
public class stores {               //门店表
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "int(255) COMMENT '门店名称'")
    private String storesName;
    @Column(columnDefinition = "int(255) COMMENT '门店地址'")
    private String storesAddress;
    @Column(columnDefinition = "int(11) COMMENT '手机号'")
    private Integer phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoresName() {
        return storesName;
    }

    public void setStoresName(String storesName) {
        this.storesName = storesName;
    }

    public String getStoresAddress() {
        return storesAddress;
    }

    public void setStoresAddress(String storesAddress) {
        this.storesAddress = storesAddress;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }
}
