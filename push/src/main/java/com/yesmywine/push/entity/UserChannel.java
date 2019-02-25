package com.yesmywine.push.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by light on 2017/4/5.
 */
@Entity
@Table(name = "userChannel")
public class UserChannel extends BaseEntity<Integer> {

    @Column(columnDefinition = "varchar(50) COMMENT '用户id'")
    private String userId;
    @Column(columnDefinition = "varchar(50) COMMENT '设备id'")
    private String channelId;
    @Column(columnDefinition = "int(5) COMMENT '设置设备类型，deviceType => 1 for web, 2 for pc, 3 for android, 4 for ios, 5 for wp'")
    private Integer deviceType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }
}
