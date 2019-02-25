package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "activitySecent")
public class ActivitySecent extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '栏目id'")
    private Integer columnId;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}
