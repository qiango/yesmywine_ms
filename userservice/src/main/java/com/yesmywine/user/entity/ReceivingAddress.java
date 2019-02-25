package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/5/3.
 */
@Entity
@Table(name = "receivingAddress")
public class ReceivingAddress extends BaseEntity<Integer>{
    @Column(columnDefinition = "varchar(10) COMMENT '收货人'")
    private String receiver; //收货人
    @Column(columnDefinition = "int(5) COMMENT '省id'")
    private Integer provinceId;//省id
    @Column(columnDefinition = "varchar(10) COMMENT '省'")
    private String province;//省
    @Column(columnDefinition = "int(5) COMMENT '市id'")
    private Integer cityId;//市id
    @Column(columnDefinition = "varchar(10) COMMENT '市'")
    private String city;//市
    @Column(columnDefinition = "int(5) COMMENT '区id'")
    private Integer areaId;//区id
    @Column(columnDefinition = "varchar(10) COMMENT '区'")
    private String area;//区
    @Column(columnDefinition = "varchar(100) COMMENT '详细地址'")
    private String detailedAddress;//详细地址
    @Column(columnDefinition = "varchar(15) COMMENT '手机号'")
    private String  phoneNumber;//手机号码
    @Column(columnDefinition = "varchar(10) COMMENT '固定电话'")
    private String fixedTelephone;//固定电话（选填）
    @Column(columnDefinition = "varchar(50) COMMENT '邮箱选填'")
    private String mailbox;  //邮箱（选填）
    @Column(columnDefinition = "varchar(10) COMMENT '地址别名'")
    private String addressAlias;//地址别名（选填）
    @Column(columnDefinition = "int(11) COMMENT '地址别名'")
    private Integer userId;//用户Id
    @Column(columnDefinition = "int(10) COMMENT '状态：0默认地址，1不是默认地址'")
    private Integer status;//1默认地址，0不是默认地址

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFixedTelephone() {
        return fixedTelephone;
    }

    public void setFixedTelephone(String fixedTelephone) {
        this.fixedTelephone = fixedTelephone;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getAddressAlias() {
        return addressAlias;
    }

    public void setAddressAlias(String addressAlias) {
        this.addressAlias = addressAlias;
    }
}
