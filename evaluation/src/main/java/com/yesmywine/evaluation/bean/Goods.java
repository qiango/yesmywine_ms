package com.yesmywine.evaluation.bean;


import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;

/**
 * Created by hz on 5/31/17.
 */
@Entity
@Table(name = "goods")
public class Goods  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;
    @Column(columnDefinition = "varchar(255) COMMENT '商品id'")
    private String picture;
    @Column(columnDefinition = "varchar(11) COMMENT '商品原价'")
    private String price;
    @Column(columnDefinition = "varchar(11) COMMENT '商品现价'")
    private String salePrice;
    @Column(columnDefinition = "int(11) COMMENT '(上架状态)0未上架,1已上架,2已下架'")
    private Integer goStatus;//(上架状态)0未上架,1已上架,2已下架
    @Column(columnDefinition = "varchar(255) COMMENT '商品名称'")
    private String name;//商品名字
    @Column(columnDefinition = "varchar(255) COMMENT '商品名称'")
    private String enName;//商品英文名字
    @Column(columnDefinition = "varchar(50) COMMENT '商品描述'")
    private String content;//商品描述
    @Column(columnDefinition = "int(11) COMMENT '评论数'")
    private Integer comment;//评论数
    @Column(columnDefinition = "int(11) COMMENT '销量'")
    private Integer sales;//销量
    @Ignore
    @Transient
    private String evaluation;//评论内容

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Integer getGoStatus() {
        return goStatus;
    }

    public void setGoStatus(Integer goStatus) {
        this.goStatus = goStatus;
    }
}
