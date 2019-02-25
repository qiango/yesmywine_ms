package com.yesmywine.goods.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sjq on 2016/12/22.
 * 商品下发类
 */
@Entity
@Table(name = "goodsSend")
public class GoodsSend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键id
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;//商品id
    @Column(columnDefinition = "varchar(20) COMMENT '商品名称'")
    private String goodsName;//商品名称
    @Column(columnDefinition = "varchar(50) COMMENT '商品编号'")
    private String goodsNo;//商品编号
    @Column(columnDefinition = "varchar(20) COMMENT '渠道名称'")
    private String channelsName;//渠道名称
    @Column(columnDefinition = "varchar(11) COMMENT '销售模式'")
    private String salesModel;//销售模式
    @Column(columnDefinition = "DATETIME COMMENT '下发时间'")
    private Date  createTime;//下发时间

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getChannelsName() {
        return channelsName;
    }

    public void setChannelsName(String channelsName) {
        this.channelsName = channelsName;
    }

    public String getSalesModel() {
        return salesModel;
    }

    public void setSalesModel(String salesModel) {
        this.salesModel = salesModel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
