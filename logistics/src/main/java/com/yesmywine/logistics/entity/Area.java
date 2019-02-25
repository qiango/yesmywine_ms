package com.yesmywine.logistics.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by ${shuang} on 2016/12/14.
 */
@Entity
@Table(name = "area")
public class Area extends BaseEntity<Integer> {
    //    private Integer parentId;
    @Column(columnDefinition = "int(2) COMMENT '行政区划代码(地区编码)'")
    private Integer areaNo;
    @Column(columnDefinition = "varchar(50) COMMENT '城市名称'")
    private String cityName;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "parentId", referencedColumnName = "areaNo")
    private Area parentName;
    @Column(columnDefinition = "varchar(50) COMMENT '等级'")
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(Integer areaNo) {
        this.areaNo = areaNo;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Area getParentName() {
        return parentName;
    }

    public void setParentName(Area parentName) {
        this.parentName = parentName;
    }
}