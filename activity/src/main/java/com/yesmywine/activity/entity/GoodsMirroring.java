package com.yesmywine.activity.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by SJQ on 2017/6/9.
 * 商品镜像表
 */
@Entity
@Table(name = "goodsMirroring")
public class GoodsMirroring extends BaseEntity<Integer> {
    @Column(columnDefinition = " varchar(255) COMMENT '商品Id' ")
    private String goodsId;
    @Column(columnDefinition = " varchar(255) COMMENT '商品编码' ")
    private String goodsCode;
    @Column(columnDefinition = " longtext COMMENT '商品图片信息' ")
    private String goodsImageUrl;
    @Column(columnDefinition = " varchar(255) COMMENT '商品名称' ")
    private String goodsName;
    @Column(columnDefinition = " varchar(255) COMMENT '商品原始名称' ")
    private String goodsOriginalName;
    @Column(columnDefinition = " varchar(11) COMMENT '商品英文名' ")
    private String goodsEnName;
    @Column(columnDefinition = " varchar(11) COMMENT '商品详情' ")
    private String goodsDetails;
    @Column(columnDefinition = " varchar(11) COMMENT '原始价格' ")
    private String price;
    @Column(columnDefinition = " varchar(11) COMMENT '销售价格' ")
    private String salePrice;
    @Column(columnDefinition = " varchar(11) COMMENT '参考价' ")
    private String referencePrice;
    @Column(columnDefinition = " varchar(11) COMMENT '分类id' ")
    private String categoryId;
    @Column(columnDefinition = " varchar(11) COMMENT '品牌对应的属性值id' ")
    private String brandId;
    @Column(columnDefinition = " varchar(2) COMMENT '(上架状态)0未上架,1已上架,2已下架' ")
    private String goStatus;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "goodsId",referencedColumnName = "goodsId")
    private Set<GoodsSku> skuInfo = new HashSet<>();
    @Column(columnDefinition = " bit(1) COMMENT '是否是会员商品' ")
    private Boolean isMember;
    @Column(columnDefinition = " bit(1) COMMENT '是否支持存酒库' ")
    private Boolean isKeep;
    @Column(columnDefinition = " varchar(2) COMMENT '销售模式 １为预售，０为普通商品,2-抢购'")
    private String saleModel;
    @Column(columnDefinition = " varchar(10) COMMENT '商品类型 single-单品 plural-组合商品 luckBage-福袋' ")
    private String goodsType;
    @Column(columnDefinition = "varchar(20) COMMENT '虚拟商品类型,giftCard：礼品卡类型'")
    private String virtualType;

    @Transient
    private Boolean isGift;

    public GoodsMirroring() {
    }

    public GoodsMirroring(String goodsId,String goodsCode, String goodsImageUrl, String goodsName,
                          String goodsOriginalName, String goodsEnName,
                          String goodsDetails, String price, String salePrice,
                          String referencePrice, String categoryId, String brandId,
                          String goStatus,Set skuInfo,Boolean isMember,
                          Boolean isKeep,String saleModel,String goodsType,String virtualType) {
        this.goodsId = goodsId;
        this.goodsCode = goodsCode;
        this.goodsImageUrl = goodsImageUrl;
        this.goodsName = goodsName;
        this.goodsOriginalName = goodsOriginalName;
        this.goodsEnName = goodsEnName;
        this.goodsDetails = goodsDetails;
        this.price = price;
        this.salePrice = salePrice;
        this.referencePrice = referencePrice;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.goStatus = goStatus;
        this.skuInfo = skuInfo;
        this.isMember = isMember;
        this.isKeep = isKeep;
        this.saleModel = saleModel;
        this.goodsType = goodsType;
        this.virtualType = virtualType;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getSaleModel() {
        return saleModel;
    }

    public void setSaleModel(String saleModel) {
        this.saleModel = saleModel;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsImageUrl() {
        return goodsImageUrl;
    }

    public void setGoodsImageUrl(String goodsImageUrl) {
        this.goodsImageUrl = goodsImageUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsOriginalName() {
        return goodsOriginalName;
    }

    public void setGoodsOriginalName(String goodsOriginalName) {
        this.goodsOriginalName = goodsOriginalName;
    }

    public String getGoodsEnName() {
        return goodsEnName;
    }

    public void setGoodsEnName(String goodsEnName) {
        this.goodsEnName = goodsEnName;
    }

    public String getGoodsDetails() {
        return goodsDetails;
    }

    public void setGoodsDetails(String goodsDetails) {
        this.goodsDetails = goodsDetails;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(String referencePrice) {
        this.referencePrice = referencePrice;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getGoStatus() {
        return goStatus;
    }

    public void setGoStatus(String goStatus) {
        this.goStatus = goStatus;
    }

    public Set<GoodsSku> getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(Set<GoodsSku> skuInfo) {
        this.skuInfo = skuInfo;
    }

    public Boolean getMember() {
        return isMember;
    }

    public void setMember(Boolean member) {
        isMember = member;
    }

    public Boolean getKeep() {
        return isKeep;
    }

    public void setKeep(Boolean keep) {
        isKeep = keep;
    }

    public Boolean getGift() {
        return isGift;
    }

    public void setGift(Boolean gift) {
        isGift = gift;
    }

    public String getVirtualType() {
        return virtualType;
    }

    public void setVirtualType(String virtualType) {
        this.virtualType = virtualType;
    }
}
