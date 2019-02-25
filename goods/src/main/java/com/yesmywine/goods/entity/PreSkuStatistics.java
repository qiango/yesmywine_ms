package com.yesmywine.goods.entity;


import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by by on 2017/7/12.
 * 预售商品sku统计表
 */
@Entity
@Table(name = "preSkuStatistics")
public class PreSkuStatistics extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(50) COMMENT 'sku编码'")
    private String skuCode;
    @Column(columnDefinition = "int(11) COMMENT '预售数量'")
    private Integer preCount;
    @Column(columnDefinition = "int(11) COMMENT '采购进货量'")
    private Integer stockCount;

    public PreSkuStatistics() {
    }

    public PreSkuStatistics(String skuCode, Integer preCount, Integer stockCount) {
        this.skuCode = skuCode;
        this.preCount = preCount;
        this.stockCount = stockCount;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Integer getPreCount() {
        return preCount;
    }

    public void setPreCount(Integer preCount) {
        this.preCount = preCount;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }
}
