package com.yesmywine.evaluation.bean;

import javax.persistence.*;

/**
 * Created by hz on 6/21/17.
 */
@Entity
@Table(name = "lable")
public class Lable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(11) COMMENT '标签名称'")
    private String lableName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLableName() {
        return lableName;
    }

    public void setLableName(String lableName) {
        this.lableName = lableName;
    }
}
