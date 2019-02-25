package com.yesmywine.user.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.user.IdUtil;
import com.yesmywine.user.dao.CouponDao;
import com.yesmywine.user.dao.UserCouponDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.Coupon;
import com.yesmywine.user.entity.UserCoupon;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.service.CouponService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ${shuang} on 2017/4/13.
 */
@Service
public class CouponServiceImpl extends BaseServiceImpl<Coupon, Integer> implements CouponService {

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private UserCouponServiceImpl userCouponServiceImpl;
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private UserCouponDao userCouponDao;


    @Override
    public String couponCreate(Map<String, String> params) throws YesmywineException {
//        以下都是判断创建优惠券时必填字段是否为空
        Coupon coupon = new Coupon();
        if (ValueUtil.notEmpity(params.get("categroy"))) {
            coupon.setCategoryId(Integer.parseInt(params.get("categroy")));
        } else {
            coupon.setCategoryId(0);
        }

        if (ValueUtil.notEmpity(params.get("brand"))) {
            coupon.setBrandId(Integer.parseInt(params.get("brand")));
        } else {
            coupon.setBrandId(0);
        }

        if (ValueUtil.notEmpity(params.get("describes"))) {
            coupon.setDescribes(params.get("describes"));
        }
        coupon.setCouponName(params.get("couponName"));
        if (ValueUtil.isEmpity(params.get("notice"))) {
            coupon.setNotice(null);
        } else {
            coupon.setNotice(Integer.valueOf(params.get("notice")));
        }
        //判断优惠券的有效期，和领取日期是否冲突
        coupon.setAmount(Integer.parseInt(params.get("amount")));
        coupon.setFull(Integer.parseInt(params.get("full")));
        coupon.setCut(Integer.parseInt(params.get("cut")));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String activeStartTime = params.get("activeStartTime");
        String activeEndTime = params.get("activeEndTime");
        String drawStartTime = params.get("drawStartTime");
        String drawEndTime = params.get("drawEndTime");
        try {
            Date activeStart = df.parse(activeStartTime);
            Date activeEnd = df.parse(activeEndTime);
            Date drawStart = df.parse(drawStartTime);
            Date drawEnd = df.parse(drawEndTime);
            if (activeStart.before(drawStart) || activeEnd.before(drawEnd)) {
                return "500";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //保存优惠券
        coupon.setUserCount(1);
        coupon.setActiveStartTime(params.get("activeStartTime"));
        coupon.setActiveEndTime(params.get("activeEndTime"));
        coupon.setDrawStartTime(params.get("drawStartTime"));
        coupon.setDrawEndTime(params.get("drawEndTime"));
        coupon.setArea(params.get("area"));
        coupon.setPlatforms(params.get("platforms"));
        Coupon coupon1 = couponDao.save(coupon);
        Long orderNo = IdUtil.genId("yyMMdd1{s}{s}{s}{r}{r}{s}{s}{r}{r}", coupon1.getId(), 5);
        coupon1.setCpCode(orderNo.toString());
        couponDao.save(coupon1);
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }


    @Override
    public String cancel(Integer couponId) throws YesmywineException {//下架优惠券
        Coupon coupon = couponDao.findOne(couponId);
        coupon.setAuditStatus(3);
        couponDao.save(coupon);
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "下架成功");
    }

    @Override
    public String delete(Integer couponId) throws YesmywineException {//删除优惠券
        Coupon coupon = couponDao.findOne(couponId);
//        判断优惠券是否被人领取或者是否已经上架
        if (coupon.getAuditStatus().equals(4)||coupon.getAuditStatus().equals(1)) {
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR, "优惠券使用中");
        } else {
            coupon.setAuditStatus(5);
            couponDao.save(coupon);
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, "success");
        }

    }

    @Override
    public List frontIndex(Integer pageNo, Integer pageSize) throws YesmywineException {//前台领取界面展示
//        仿照京东的优惠券领取界面展示
        List<Coupon> coupons = couponDao.findSome();
        List<Map<String, String>> newList = new ArrayList<>();
        for (int i = 0; i < coupons.size(); i++) {
            Map<String, String> map = new HashMap<>();
            Coupon coupon = coupons.get(i);
            Integer total = coupon.getAmount();
            Integer haveDraw = coupon.getUsedDrawCount();
            map.put("full", coupon.getFull().toString());
            map.put("cut", coupon.getCut().toString());
            map.put("platforms", coupon.getPlatforms());
            map.put("remain", "");
            if (coupon.getCategoryId() != 0) {
                // 通过分类Id查分类名称
                String categoryName = userCouponServiceImpl.http(coupon.getCategoryId(), null);
                map.put("categoryName", categoryName);
            } else {
                map.put("categoryName", "全品类可用");
            }
            newList.add(map);
        }
        //以下为分页
        List<Map<String, String>> result = new ArrayList<>();
        if (newList != null && newList.size() > 0) {
            int allCount = newList.size();
            int pageCount = (allCount + pageSize - 1) / pageSize;
            if (pageNo >= pageCount) {
                pageNo = pageCount;
            }
            int start = (pageNo - 1) * pageSize;
            int end = pageNo * pageSize;
            if (end >= allCount) {
                end = allCount;
            }
            for (int i = start; i < end; i++) {
                result.add(newList.get(i));
            }
        }
        return (result != null && result.size() > 0) ? result : null;
    }

    @Override
    public String audit(Integer couponId, Integer auditStatus, String remarks) throws YesmywineException {
//        以下为优惠券审查审核操作
        Coupon coupon = couponDao.findOne(couponId);
        coupon.setAuditStatus(auditStatus);
        coupon.setRemarks(remarks);
        couponDao.save(coupon);
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }

    @Override
    public String filter(String couponIds, String username,String status) throws YesmywineException {
        //活动展示的优惠券过滤，活动那边传参数，然后判断哪些优惠券可以使用并返回给活动
        String[] array = couponIds.split(",");
        List<Coupon> coupons = new ArrayList<>();
        if(status!=null){
            for (int i = 0; i < array.length; i++) {
                Integer couponId = Integer.valueOf(array[i]);
                Coupon coupon = couponDao.findOne(couponId);
               coupons.add(coupon);
            }
        }else {
            if (username==null) {
                for (int i = 0; i < array.length; i++) {
                    Integer couponId = Integer.valueOf(array[i]);
                    Coupon coupon = couponDao.findOne(couponId);
                    if (ValueUtil.notEmpity(coupon)) {
                        Integer amount = coupon.getAmount();//总数量
                        Integer auditStatus = coupon.getAuditStatus();//审核
                        if (amount > 0 && (auditStatus == 1 || auditStatus == 4)) {
                            coupons.add(coupon);
                        }
                    }
                }
            } else {
                UserInformation userInformation = userInformationDao.findByUserName(username);
                Integer userId = userInformation.getId();
                for (int i = 0; i < array.length; i++) {
                    Integer couponId = Integer.valueOf(array[i]);
                    Coupon coupon = couponDao.findOne(couponId);
                    if (ValueUtil.notEmpity(coupon)) {
                        Integer amount = coupon.getAmount();//总数量
                        Integer auditStatus = coupon.getAuditStatus();//审核状态
                        if (amount > 0 && (auditStatus == 1 || auditStatus == 4)) {
                            List<UserCoupon> list = userCouponDao.findByUserIdAndCouponId(userId, couponId);
                            Integer userCount = coupon.getUserCount();//每人可以领取数量
                            Integer hasDraw = coupon.getUsedDrawCount();//已经被领取的
                            if (hasDraw < amount && list.size() < userCount) {
                                coupons.add(coupon);
                            }
                        }
                    }
                }
            }
        }

        return ValueUtil.toJson(HttpStatus.SC_CREATED, coupons);
    }

    @Override
    public void task() throws YesmywineException {
//        优惠券自动过期操作，定时操作。
       List<Coupon> list = couponDao.findAll();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String currentDate1 = df.format(new Date());//当前日期
        List<Coupon> list1 = new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            Coupon coupon = list.get(i);
            String activeStartTime = coupon.getActiveStartTime();
            String activeEndTime = coupon.getActiveEndTime();
            try {
                Date currentDate = (Date) df.parseObject(currentDate1);
                Date Start = (Date) df.parseObject(activeStartTime);
                Date End = (Date) df.parseObject(activeEndTime);
                if ((currentDate.after(Start) && currentDate.before(End))) {//在日期内
                }else {
                    coupon.setStatus(3);
                    list1.add(coupon);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        couponDao.save(list1);
    }


}
