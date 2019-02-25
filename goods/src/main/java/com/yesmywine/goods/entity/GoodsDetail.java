package com.yesmywine.goods.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hz on 6/27/17.
 */
@Entity
@Table(name = "goodsDetail")
public class GoodsDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private  Integer goodsId;
    @Lob
//    @Column(columnDefinition = "varchar(255) COMMENT '商品细节'")
    private String goodsDetail;
    @Column(columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

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

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
