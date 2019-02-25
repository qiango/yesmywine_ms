package com.yesmywine.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "goods")
public class Goods{

    @Id
    private Integer id;
    @Column(columnDefinition = "varchar(255) COMMENT '商品名'")
    private String goodsName;
    @Column(columnDefinition = "varchar(255) COMMENT '商品图片'")
    private String picture;
    @Column(columnDefinition = "int(11) COMMENT '销量'")
    private Integer sales;//销量
    @Column(columnDefinition = "varchar(255) COMMENT '原价'")
    private String price;//原价
    @Column(columnDefinition = "varchar(255) COMMENT '现价'")
    private String salePrice;//现价
    @Column(columnDefinition = "int(11) COMMENT '(上架状态)0未上架,1已上架,2已下架'")
    private Integer goStatus;//(上架状态)0未上架,1已上架,2已下架
    @Column(columnDefinition = "int(11) COMMENT '分类id'")
    private Integer categoryId;//分类id
    @Column(columnDefinition = "varchar(255) COMMENT '分类名称'")
    private String categoryName;//分类名称
    @Column(columnDefinition = "varchar(255) COMMENT '商品英文名'")
    private String goodsEnName;//商品英文名
    @Column(columnDefinition = "int(11) COMMENT '评论数'")
    private Integer comments;//评论数
    @Column(columnDefinition = "double COMMENT '好评率'")
    private Double praise;//好评率

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
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

    public String getGoodsEnName() {
        return goodsEnName;
    }

    public void setGoodsEnName(String goodsEnName) {
        this.goodsEnName = goodsEnName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Double getPraise() {
        return praise;
    }

    public void setPraise(Double praise) {
        this.praise = praise;
    }

    public Integer getGoStatus() {
        return goStatus;
    }

    public void setGoStatus(Integer goStatus) {
        this.goStatus = goStatus;
    }
}
