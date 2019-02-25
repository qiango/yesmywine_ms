package com.yesmywine.goods.entity;


import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.goods.bean.IsUse;
import com.yesmywine.goods.entityProperties.Category;
import com.yesmywine.goods.entityProperties.Supplier;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WANG, RUIQING on 12/7/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "sku")
public class Sku extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(100) COMMENT '编码'")
    private String code;
//    private Integer categoryId;
    @Column(columnDefinition = "varchar(255) COMMENT 'sku名称'")
    private String skuName;
//    private Integer supplierId; //供应商id
    @Enumerated(EnumType.ORDINAL)
    private IsUse isUse;
    @Column(columnDefinition = "int(2) COMMENT '类型'")
    private Integer type;
    @Column(columnDefinition = "double COMMENT '成本价'")
    private Double costPrice;//成本价
    @Lob
    @Column(columnDefinition="TEXT")
    private String image;//图片
    @OneToMany(cascade =CascadeType.ALL)
    @JoinColumn(name = "skuPropId")//sku属性
    private List<SkuProp> skuProp = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "skuCommonPropId")//sku普通属性
    private List<SkuCommonProp> skuCommonProp = new ArrayList<>();
    @Lob
    @Ignore
    @Transient
    private String property;
    @Lob
    private String sku;
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "supplierId")
    private Supplier supplier;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "categoryId")
    private Category category;
    @Column(columnDefinition = "int(2) COMMENT '是否是贵品,0:是,1:否'")
    private Integer isExpensive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }


    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public IsUse getIsUse() {
        return isUse;
    }

    public void setIsUse(IsUse isUse) {
        this.isUse = isUse;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public List<SkuProp> getSkuProp() {
        return skuProp;
    }

    public void setSkuProp(List<SkuProp> skuProp) {
        this.skuProp = skuProp;
    }

    public List<SkuCommonProp> getSkuCommonProp() {
        return skuCommonProp;
    }

    public Integer getIsExpensive() {
        return isExpensive;
    }

    public void setIsExpensive(Integer isExpensive) {
        this.isExpensive = isExpensive;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSkuCommonProp(List<SkuCommonProp> skuCommonProp) {
        this.skuCommonProp = skuCommonProp;
    }
}
