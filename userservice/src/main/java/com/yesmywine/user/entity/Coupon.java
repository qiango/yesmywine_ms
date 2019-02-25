package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by hz on 12/19/16.
 */
@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity<Integer> {   //优惠券

    @Column(columnDefinition = "varchar(100) COMMENT '优惠券名称'")
    private String couponName;
    @Column(columnDefinition = "int(11) COMMENT '优惠券名称'")
    private Integer amount;//数量
    @Column(columnDefinition = "varchar(200) COMMENT '优惠券描述'")
    private String describes;//描述
    @Column(columnDefinition = "int(11) COMMENT '满多少'")
    private Integer full;//满
    @Column(columnDefinition = "int(11) COMMENT '减多少'")
    private Integer cut;//减
    @Column(columnDefinition = "varchar(11) COMMENT '平台：全平台，网站，app'")
    private String platforms;//全平台/网站/app
    @Column(columnDefinition = "varchar(11) COMMENT '地域：全国'")
    private String area;//全平台/网站/app
    @Column(columnDefinition = "int(11) COMMENT '分类ID'")
    private Integer categoryId;//分类
    @Column(columnDefinition = "int(11) COMMENT '品牌ID'")
    private Integer brandId;//品牌
    @Column(columnDefinition = "int(5) COMMENT '单人领取张数'")
    private Integer userCount;//一个人可以领取的次数
    @Column(columnDefinition = "varchar(11) COMMENT '领取开始时间'")
    private String drawStartTime;//领取开始时间 2017-4-1;2017-4-25
    @Column(columnDefinition = "varchar(11) COMMENT '领取结束时间'")
    private String drawEndTime;//领取结束时间 2017-4-1;2017-4-25
    @Column(columnDefinition = "varchar(11) COMMENT '有效开始时间'")
    private String activeStartTime;//有效开始时间 2017-4-1;2017-4-25
    @Column(columnDefinition = "varchar(11) COMMENT '有效结束时间'")
    private String activeEndTime;//有效结束时间 2017-4-1;2017-4-25
    @Column(columnDefinition = "int(1) COMMENT '状态：待审核0，审核通过1，审核不通过2,下架3,被人领取4,5删除'")
    private Integer auditStatus;//待审核0，审核通过1，审核不通过2,下架3,被人领取4,5shanchu
    @Column(columnDefinition = "int(1) COMMENT '通知方式'")
    private Integer notice;//通知方式
    @Column(columnDefinition = "int(11) COMMENT '已经领取'")
    private Integer usedDrawCount;//已经领取
    @Column(columnDefinition = "varchar(255) COMMENT '优惠券标识'")
    private String  remarks;
    @Column(columnDefinition = "int(1) COMMENT '冗余字段'")
    private Integer  status;
    @Column(columnDefinition = "varchar(255) COMMENT '编码'")
    private String  cpCode;

    public Coupon(){
        this.area="全国";
        this.platforms="全平台";
        this.categoryId =0;//全部分类
        this.brandId=0;//全品牌
        this.usedDrawCount=0;//默认没人领取
        this.auditStatus = 0;//待审核
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCpCode() {
        return cpCode;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public Integer getFull() {
        return full;
    }

    public void setFull(Integer full) {
        this.full = full;
    }

    public Integer getCut() {
        return cut;
    }

    public void setCut(Integer cut) {
        this.cut = cut;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getDrawStartTime() {
        return drawStartTime;
    }

    public void setDrawStartTime(String drawStartTime) {
        this.drawStartTime = drawStartTime;
    }

    public String getDrawEndTime() {
        return drawEndTime;
    }

    public void setDrawEndTime(String drawEndTime) {
        this.drawEndTime = drawEndTime;
    }

    public String getActiveStartTime() {
        return activeStartTime;
    }

    public void setActiveStartTime(String activeStartTime) {
        this.activeStartTime = activeStartTime;
    }

    public String getActiveEndTime() {
        return activeEndTime;
    }

    public void setActiveEndTime(String activeEndTime) {
        this.activeEndTime = activeEndTime;
    }

    public Integer getNotice() {
        return notice;
    }

    public void setNotice(Integer notice) {
        this.notice = notice;
    }

    public Integer getUsedDrawCount() {
        return usedDrawCount;
    }

    public void setUsedDrawCount(Integer usedDrawCount) {
        this.usedDrawCount = usedDrawCount;
    }
}
