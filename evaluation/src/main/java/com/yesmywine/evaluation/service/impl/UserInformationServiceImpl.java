//package com.hzbuvi.evaluation.service.impl;
//
//
//import BaseServiceImpl;
//import Goods;
//import com.hzbuvi.evaluation.bean.UserInformation;
//import GoodsDao;
//import com.hzbuvi.evaluation.dao.UserInformationDao;
//import com.hzbuvi.evaluation.service.UserInformationService;
//import ValueUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//
///**
// * Created by hz on 12/8/16.
// */
//@Service
//public class UserInformationServiceImpl extends BaseServiceImpl<UserInformation, Integer> implements UserInformationService {
//    @Autowired
//    private UserInformationDao userInformationDao;
//
//    public String synchronous(Map<String,Object> map){
//        if(0==Integer.parseInt(String.valueOf(map.get("synchronous")))){
//            UserInformation userInformation = new UserInformation();
//            if(ValueUtil.notEmpity(map.get("userName"))){
//                userInformation.setUserName(String.valueOf(map.get("userName")));
//            }else {
//                return "add erro";
//            }
//            userInformation.setId((Integer) map.get("id"));
//
//            userInformationDao.save(userInformation);
//            return "add success";
//        }else {
//            Integer id = Integer.parseInt(String.valueOf(map.get("id")));
//            userInformationDao.delete(id);
//            return "delete success";
//        }
//    }
//}
//
//
//
