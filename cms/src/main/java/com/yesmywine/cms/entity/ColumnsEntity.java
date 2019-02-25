package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by liqingqing on 2017/1/3.
 */
@Entity
@Table(name = "columns")
public class ColumnsEntity extends BaseEntity<Integer>{

    @Column(columnDefinition = "varchar(200) COMMENT '栏目名'")
    private String columnsName;
//    @ManyToOne(cascade = CascadeType.REFRESH)
//    @JoinColumn(name = "parentName")
//    private ColumnsEntity columnsEntity;
    @Column(columnDefinition = "int(11) COMMENT '父id'")
    private Integer pId;//父id
    @Column(columnDefinition = "varchar(200) COMMENT '父名称'")
    private String parentName;
    @Column(columnDefinition = "varchar(50) COMMENT '编码'")
    private String code;


    public String getColumnsName() {
        return columnsName;
    }

    public void setColumnsName(String columnsName) {
        this.columnsName = columnsName;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
