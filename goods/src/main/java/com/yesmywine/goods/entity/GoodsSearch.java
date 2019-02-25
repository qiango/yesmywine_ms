package com.yesmywine.goods.entity;

import org.apache.solr.client.solrj.beans.Field;
import javax.persistence.*;
import java.util.Date;

public class GoodsSearch {

    @Id
    @Field("id")
    private Integer id;
    @Field("goodsImageUrl")
    private String goodsImageUrl;  //商品图片url
    @Field("goodsName")
    private String goodsName;//商品名称
    @Field("goodsEnName")
    private String goodsEnName;//商品英文名
    @Field("price")
    private Double price;//原始价格
    @Field("salePrice")
    private Double salePrice;//销售价格
    @Field("sales")
    private Integer sales;//销量
    @Field("listedTime")
    private Date listedTime;//上架时间
    @Field("comments")
    private Integer comments;//评论数
    @Field("praise")
    private double praise;//好评率
    @Field("goodsSkuCode")
    private String goodsSkuCode;
    @Field("goodsSkuString")
    private String goodsSkuString;
    @Field("categoryIdGrandpa")
    private Integer categoryIdGrandpa;
    @Field("categoryIdParent")
    private Integer categoryIdParent;
    @Field("categoryId")
    private Integer categoryId;//分类id
    @Field("categoryName")
    private String categoryName;//分类名称
    @Field("goodsCategoryCode")
    private String goodsCategoryCode;//商品属性code
    @Field("goodsCategoryString")
    private String goodsCategoryString;//商品属性String
    @Field("goodsPropCode")
    private String goodsPropCode;//商品属性code
    @Field("goodsPropString")
    private String goodsPropString;//商品属性String
    @Field("goStatus")
    private Integer goStatus;//(上架状态)0未上架,1已上架,2已下架

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getGoodsEnName() {
        return goodsEnName;
    }

    public void setGoodsEnName(String goodsEnName) {
        this.goodsEnName = goodsEnName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getGoodsSkuCode() {
        return goodsSkuCode;
    }

    public void setGoodsSkuCode(String goodsSkuCode) {
        this.goodsSkuCode = goodsSkuCode;
    }

    public String getGoodsSkuString() {
        return goodsSkuString;
    }

    public void setGoodsSkuString(String goodsSkuString) {
        this.goodsSkuString = goodsSkuString;
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

    public String getGoodsPropCode() {
        return goodsPropCode;
    }

    public void setGoodsPropCode(String goodsPropCode) {
        this.goodsPropCode = goodsPropCode;
    }

    public String getGoodsPropString() {
        return goodsPropString;
    }

    public void setGoodsPropString(String goodsPropString) {
        this.goodsPropString = goodsPropString;
    }


//    public StringBuffer getGoodsPropString() {
//        return goodsPropString;
//    }
//
//    public void setGoodsPropString(StringBuffer goodsPropString) {
//        this.goodsPropString = goodsPropString;
//    }

    public Integer getGoStatus() {
        return goStatus;
    }

    public void setGoStatus(Integer goStatus) {
        this.goStatus = goStatus;
    }

    public Date getListedTime() {
        return listedTime;
    }

    public void setListedTime(Date listedTime) {
        this.listedTime = listedTime;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public String getGoodsCategoryCode() {
        return goodsCategoryCode;
    }

    public void setGoodsCategoryCode(String goodsCategoryCode) {
        this.goodsCategoryCode = goodsCategoryCode;
    }

    public String getGoodsCategoryString() {
        return goodsCategoryString;
    }

    public void setGoodsCategoryString(String goodsCategoryString) {
        this.goodsCategoryString = goodsCategoryString;
    }

    public double getPraise() {
        return praise;
    }

    public void setPraise(double praise) {
        this.praise = praise;
    }

    public Integer getCategoryIdGrandpa() {
        return categoryIdGrandpa;
    }

    public void setCategoryIdGrandpa(Integer categoryIdGrandpa) {
        this.categoryIdGrandpa = categoryIdGrandpa;
    }

    public Integer getCategoryIdParent() {
        return categoryIdParent;
    }

    public void setCategoryIdParent(Integer categoryIdParent) {
        this.categoryIdParent = categoryIdParent;
    }
}
