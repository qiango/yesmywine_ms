//package com.hzbuvi.logistics.controller;
//
//import Get;
//import GetResponse;
//import ZJSTracking;
//import ZJSTrackingSoap;
//import com.hzbuvi.logistics.vientiane.*;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
///**
// * Created by wangdiandian on 2017/5/12.
// */
//@RestController
//@RequestMapping("/logistics/express")
//public class ExpressController {
//
//
//
////    @RequestMapping(method = RequestMethod.POST)
////    public String create(@RequestParam Map<String, String> param) {//宅急送物流信息
//////        try {
////////            return ValueUtil.toJson(HttpStatus.SC_CREATED, );
//////        } catch (YesmywineException e) {
//////            return ValueUtil.toError(e.getCode(),e.getMessage());
//////        }
////    }
//    public static void main(String[] args) throws Exception {
//
////        EwinshineInte ewinshineInte = new EwinshineInte();
////        EwinshineIntePortType ewinDEFAULT_INCOMPATIBLE_IMPROVEMENTSshineInteHttpPort = ewinshineInte.getEwinshineInteHttpPort();
////        String result = ewinshineInteHttpPort.getSuccSendOrderInfo("万象物流", "21285286", "2445");
////        System.out.print(result);
//
//
////       String SS="http://edi.zjs.com.cn/service/query.asmx/GetMd5?userId=MuShiMaoYi&strInfo=<BatchQueryRequest><logisticProviderID>MuShiMaoYi</logisticProviderID><orders><order><orderNo>8392393391</orderNo></order></orders></BatchQueryRequest>";
//
//        ZJSTracking zjsTracking = new ZJSTracking();
//        ZJSTrackingSoap zjsTrackingSoap = zjsTracking.getZJSTrackingSoap();
//        String xml="<BatchQueryRequest><logisticProviderID>MuShiMaoYi</logisticProviderID><orders><order><orderNo>面单号</orderNo></order></orders></BatchQueryRequest>";
//        String s = zjsTrackingSoap.get("MuShiMaoYi", "ddd", "17D09187-CB5D-4C83-8D42-1B32C1BE03A2");
////        Get get = new Get();
////        GetResponse getResponse = new GetResponse();
////        String getResult = getResponse.getGetResult();
//
//
//
//        System.out.print(s);
//
//
//    }
//}
