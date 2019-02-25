package com.yesmywine.goods.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hz on 6/27/17.
 */
@Entity
@Table(name = "goodsMirrorDetail")
public class GoodsMirrorDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    @Column(columnDefinition = "int(11) COMMENT '商品镜像id'")
    private  Integer goodsMirrorId;
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

    public Integer getGoodsMirrorId() {
        return goodsMirrorId;
    }

    public void setGoodsMirrorId(Integer goodsMirrorId) {
        this.goodsMirrorId = goodsMirrorId;
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
