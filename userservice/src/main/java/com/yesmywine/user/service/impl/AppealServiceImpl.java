package com.yesmywine.user.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.user.dao.AppealDao;
import com.yesmywine.user.dao.UserBlackDao;
import com.yesmywine.user.entity.Appeal;
import com.yesmywine.user.entity.UserBlack;
import com.yesmywine.user.service.AppealService;
import com.yesmywine.user.service.UserBlackService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ${shuang} on 2017/4/6.
 */
@Service
public class AppealServiceImpl extends BaseServiceImpl<Appeal,Integer> implements AppealService {

    @Autowired
    private AppealDao appealDao;
    @Autowired
    private UserBlackService userBlackService;
    @Autowired
    private UserBlackDao userBlackDao;

    @Override
    public String appeal(Integer userId, String justification) throws YesmywineException {
        try {
            ValueUtil.verify(userId);
            ValueUtil.verify(justification);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
        UserBlack userBlack=userBlackService.findByUserId(userId);
        Appeal appeal1= appealDao.findByUserIdAndStatus(userId,5);
        //判断能不能申诉
        if(ValueUtil.isEmpity(userBlack)||userBlack.getBlackStatus()==1){
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"不是黑名单用户不能申诉");
        }else if(ValueUtil.notEmpity(appeal1)){
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"处理中再次不能申诉");
        }
//        保留申诉记录
        Appeal appeal=new Appeal();
        appeal.setUserId(userId);
        appeal.setUserName(userBlack.getUserName());
        appeal.setContent(justification);
        appealDao.save(appeal);
        return ValueUtil.toJson(HttpStatus.SC_CREATED,"申诉成功客服正在处理");
    }

    @Override
    public String feedback(Integer userId, String feedback, Integer blackStatus) throws YesmywineException {
//        判断处理内容是不是空的
        if(ValueUtil.isEmpity(feedback)){
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"反馈不能为空");
        }
//        判断传过来的数据是不是1解除2未解除
        if(blackStatus==1||blackStatus==2) {
            Appeal appeal= appealDao.findByUserIdAndStatus(userId,5);//未处理状态
//            判断申诉记录是否存在
            if(ValueUtil.isEmpity(appeal)){
                return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"已经处理过不能再处理");
            }
            UserBlack userBlack = userBlackDao.findByUserId(userId);
            userBlack.setBlackStatus(blackStatus);
            userBlackDao.save(userBlack);
            appeal.setRejectContent(feedback);
            appeal.setStatus(4);//处理过
            appealDao.save(appeal);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,"处理成功");
        }else {
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"状态异常");
        }

    }
}
