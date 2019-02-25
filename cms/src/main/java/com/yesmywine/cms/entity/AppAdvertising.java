package com.yesmywine.cms.entity;

import javax.persistence.*;

/**
 * Created by hz on 7/5/17.
 */
@Entity
@Table(name = "appPosition")
public class AppAdvertising {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "int(11) COMMENT '顶部广告位设置'")
    private Integer topId;//顶部广告位设置
    @Column(columnDefinition = "int(11) COMMENT '重要入口下方广告位'")
    private Integer importantId;//重要入口下方广告位
    @Column(columnDefinition = "int(11) COMMENT '合作专区广告位'")
    private Integer collaborateId;//合作专区广告位

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTopId() {
        return topId;
    }

    public void setTopId(Integer topId) {
        this.topId = topId;
    }

    public Integer getImportantId() {
        return importantId;
    }

    public void setImportantId(Integer importantId) {
        this.importantId = importantId;
    }

    public Integer getCollaborateId() {
        return collaborateId;
    }

    public void setCollaborateId(Integer collaborateId) {
        this.collaborateId = collaborateId;
    }
}
