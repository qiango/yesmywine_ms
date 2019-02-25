package com.yesmywine.cms.entity;

import javax.persistence.*;

/**
 * Created by yly on 2017/1/12.
 */
@Entity
@Table(name = "adverPosition")//关联表
public class AdverPositionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键，自增
    @OneToOne
    @JoinColumn(name = "adverId")
    private AdverEntity adverEntity;//广告素材编号
    @OneToOne
    @JoinColumn(name = "positionId")
    private PositionEntity positionEntity;//广告位编号

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public AdverEntity getAdverEntity() {
        return adverEntity;
    }

    public void setAdverEntity(AdverEntity adverEntity) {
        this.adverEntity = adverEntity;
    }

    public PositionEntity getPositionEntity() {
        return positionEntity;
    }

    public void setPositionEntity(PositionEntity positionEntity) {
        this.positionEntity = positionEntity;
    }
}
