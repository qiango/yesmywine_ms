package com.yesmywine.pay.service;

import com.yesmywine.pay.entity.AlipaySetting;
import com.yesmywine.pay.entity.UnionPaySetting;
import com.yesmywine.pay.entity.WeChatPaySetting;

/**
 * Created by SJQ on 2017/3/2.
 */
public interface PaymentParamService {
    AlipaySetting findAlipayById(Integer id);

    AlipaySetting updateAlipay(AlipaySetting alipaySetting);

    UnionPaySetting findUnionPayById(Integer id);

    UnionPaySetting updateUnionPay(UnionPaySetting unionPaySetting);

    WeChatPaySetting findWeChatById(Integer id);

    WeChatPaySetting updateUnionPay(WeChatPaySetting weChatPaySetting);

    AlipaySetting getAlipay();

    UnionPaySetting getUnionPay();

    WeChatPaySetting getWeChat();
}
