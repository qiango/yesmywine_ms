package com.yesmywine.goods.entityProperties;

import javax.persistence.*;

/**
 * Created by WANG, RUIQING on 12/7/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "channel")
public class Channel {
    @Id
    private Integer id;
    @Column(columnDefinition = "varchar(20) COMMENT '销售渠道名称'")
    private String channelName;        //销售渠道名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
