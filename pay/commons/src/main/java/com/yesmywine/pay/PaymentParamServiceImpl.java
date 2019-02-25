package com.yesmywine.pay;

import com.yesmywine.pay.service.PaymentParamService;
import com.yesmywine.pay.dao.AlipaySettingDao;
import com.yesmywine.pay.dao.UnionPaySettingDao;
import com.yesmywine.pay.dao.WeChatSettingDao;
import com.yesmywine.pay.entity.AlipaySetting;
import com.yesmywine.pay.entity.UnionPaySetting;
import com.yesmywine.pay.entity.WeChatPaySetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by SJQ on 2017/3/2.
 */
@Service
public class PaymentParamServiceImpl implements PaymentParamService {
    @Autowired
    private AlipaySettingDao alipaySettingDao;
    @Autowired
    private UnionPaySettingDao unionPaySettingDao;
    @Autowired
    private WeChatSettingDao weChatSettingDao;

    @Override
    public AlipaySetting findAlipayById(Integer id) {
        return alipaySettingDao.findOne(id);
    }

    @Override
    public AlipaySetting updateAlipay(AlipaySetting alipaySetting) {
        return alipaySettingDao.save(alipaySetting);
    }

    @Override
    public UnionPaySetting findUnionPayById(Integer id) {
        return unionPaySettingDao.findOne(id);
    }

    @Override
    public UnionPaySetting updateUnionPay(UnionPaySetting unionPaySetting) {
        return unionPaySettingDao.save(unionPaySetting);
    }

    @Override
    public WeChatPaySetting findWeChatById(Integer id) {
        return weChatSettingDao.findOne(id);
    }

    @Override
    public WeChatPaySetting updateUnionPay(WeChatPaySetting weChatPaySetting) {
        return weChatSettingDao.save(weChatPaySetting);
    }

    @Override
    public AlipaySetting getAlipay() {
        return alipaySettingDao.findOne(1);
    }

    @Override
    public UnionPaySetting getUnionPay() {
        return unionPaySettingDao.findOne(1);
    }

    @Override
    public WeChatPaySetting getWeChat() {
        return weChatSettingDao.findOne(1);
    }
}
