package com.yesmywine.goods.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.goods.entityProperties.Category;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;

/**
 * Created by sjq on 2016/12/22.
 * 标签
 */
@Entity
@Table(name = "label")
public class Label extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(20) COMMENT '标签'")
    private String name;//标签
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "parentId")
    private Label parentName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Label getParentName() {
        return parentName;
    }

    public void setParentName(Label parentName) {
        this.parentName = parentName;
    }
}
