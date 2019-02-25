package com.yesmywine.evaluation.bean;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by hz on 6/21/17.
 */
@Entity
@Table(name = "evaluationLable")
public class EvaluationLable extends BaseEntity<Integer> {//商品评论标签

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lableId")
    private Lable lable;
    @Column(columnDefinition = "varchar(11) COMMENT '类型名称'")
    private Integer count;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;

    public Lable getLable() {
        return lable;
    }

    public void setLable(Lable lable) {
        this.lable = lable;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}
