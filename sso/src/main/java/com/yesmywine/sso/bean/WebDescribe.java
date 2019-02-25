package com.yesmywine.sso.bean;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/6/15.
 */
@Entity
@Table(name = "webDescribe")
public class WebDescribe extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(50) COMMENT '网站标题'")
    private  String webTitle;
    @Column(columnDefinition = "varchar(200) COMMENT '网站描述'")
    private String webDescribe;
    @Column(columnDefinition = "varchar(100) COMMENT '关键字'")
    private String keyword;
    @Column(columnDefinition = "int(5) COMMENT '自动确认收货时间'")
    private Integer days;
    @Column(columnDefinition = "int(10) COMMENT '人民币兑换成长值'")
    private Integer points;//人民币兑换成长值


    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebDescribe() {
        return webDescribe;
    }

    public void setWebDescribe(String webDescribe) {
        this.webDescribe = webDescribe;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
