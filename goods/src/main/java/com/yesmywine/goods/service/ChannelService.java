
package com.yesmywine.goods.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entityProperties.Channel;


/**
 * Created by hz on 2/10/17.
 */

public interface ChannelService extends BaseService<Channel, Integer> {

    Boolean synchronous(Integer id, String name, Integer synchronous);
}

