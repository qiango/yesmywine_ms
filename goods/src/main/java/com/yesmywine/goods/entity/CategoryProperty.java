package com.yesmywine.goods.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.goods.entityProperties.PropertiesValue;

import javax.persistence.*;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "categoryProperty")
public class CategoryProperty extends BaseEntity<Integer>{
    @Column(columnDefinition = "int(11) COMMENT '分类Id'")
    private Integer categoryId;
    @Column(columnDefinition = "int(11) COMMENT '属性Id'")
    private Integer propertyId;
    @Column(columnDefinition = "int(11) COMMENT '是否显示'")
    private Integer type;//默认是0，显示，1是不显示

    @OneToOne
    @JoinColumn(name = "propertyValueId")
    private PropertiesValue propertyValue;


    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
    }

    public PropertiesValue getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(PropertiesValue propertyValue) {
        this.propertyValue = propertyValue;
    }

    //    public Integer getPropertyValueId() {
//        return propertyValueId;
//    }
//
//    public void setPropertyValueId(Integer propertyValueId) {
//        this.propertyValueId = propertyValueId;
//    }


//    public Set<Properties> getProperties() {
//        return properties;
//    }
//
//    public void setProperties(Set<Properties> properties) {
//        this.properties = properties;
//    }
//
//    public Set<PropertiesValue> getPropertiesValues() {
//        return propertiesValues;
//    }
//
//    public void setPropertiesValues(Set<PropertiesValue> propertiesValues) {
//        this.propertiesValues = propertiesValues;
//    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
