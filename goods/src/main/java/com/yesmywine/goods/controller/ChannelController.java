
package com.yesmywine.goods.controller;

import com.yesmywine.goods.service.ChannelService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hz on 3/15/17.
 */
@RestController
@RequestMapping("/goods/channel/syn")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @RequestMapping(method = RequestMethod.POST)
    public String synchronous(String jsonData) throws  Exception{
//        Integer id, String name, Integer synchronous
        try {
            Integer id = Integer.valueOf(ValueUtil.getFromJson(jsonData,"data","id"));
            Integer synchronous = Integer.valueOf(ValueUtil.getFromJson(jsonData,"msg"));
            String name = String.valueOf(ValueUtil.getFromJson(jsonData,"data","channelName"));
            ValueUtil.verify(id,"id");
            ValueUtil.verify(synchronous,"synchronous");
            Boolean result = this.channelService.synchronous(id, name, synchronous);
            if(result){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
            }else {
                return ValueUtil.toError("500", "erro");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
}
