package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2016/12/19.
 */
@Entity
@Table(name = "orderDispatch")
public class OrderDispatch extends BaseEntity<Long> {//订单操作信息表
    @Column(columnDefinition = "BIGINT(20) COMMENT '订单编码'")
    private Long orderNo;//订单编码
    @Column(columnDefinition = "int(11) COMMENT '操作人'")
    private Integer operator;//操作人
    @Column(columnDefinition = "int(1) COMMENT '订单：0完成，1待付款，2已取消，3待发货，4（支付未发货的取消）处理中，5待收货,9取消失败/退货单：0.待审核/1.已完成/2.审核中/3.审核未通过/4.取消）'")
    private Integer status;//状态
    @Column(columnDefinition = "int(1) COMMENT '订单/退货单'")
    private Integer label;//0订单1退货单

    public Integer getLabel() {
        return label;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
