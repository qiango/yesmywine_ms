package com.yesmywine.goods.entityProperties;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hz on 1/6/17.
 */
@Entity
@Table(name = "propertiesValue")
public class PropertiesValue {
    @Id
    protected Integer id;
    @Column(columnDefinition = "DATETIME COMMENT '创建时间'")
    protected Date createTime;
    @Column(columnDefinition = "varchar(20) COMMENT '属性值'")
    private String cnValue;   //属性值
    @Column(columnDefinition = "int(11) COMMENT '属性id'")
    private Integer propertiesId;
    @Column(columnDefinition = "varchar(20) COMMENT '编码'")
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCnValue() {
        return cnValue;
    }

    public void setCnValue(String cnValue) {
        this.cnValue = cnValue;
    }

    public Integer getPropertiesId() {
        return propertiesId;
    }

    public void setPropertiesId(Integer propertiesId) {
        this.propertiesId = propertiesId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
