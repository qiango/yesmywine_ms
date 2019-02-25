package com.yesmywine.httpclient;

import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import org.junit.Test;

/**
 * Created by WANG, RUIQING on 11/28/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */

public class GetTest {



    @Test
    public void getTest() {
        HttpBean bean = new HttpBean("http://cn.bing.com", RequestMethod.get);
        bean.run();
        System.out.println(bean.getResponseContent());
    }

    //


//	@Test
//	public void getTest(){
//		Map map=new HashMap<>();
//		HttpBean bean = new HttpBean("http://cn.bing.com", RequestMethod.get);
//		bean.run();
//		System.out.println(bean.getResponseContent());
//	}
//


//    @Test
//    public void getTest() {
//        Map map = new HashMap<>();
//        HttpBean bean = new HttpBean("http://cn.bing.com", RequestMethod.get);
//        bean.run();
//        System.out.println(bean.getResponseContent());
//    }
//

//	@Test
//	public void getTest1(){
//		HttpBean bean = new HttpBean("http://139.219.226.16:8443/person/check");
//		bean.setRequestMethod(RequestMethod.get);
//		bean.run();
//		System.out.println(bean.getResponseContent());
//	}
//


    @Test
    public void postTest() {
        HttpBean bean = new HttpBean("http://88.88.88.211:8190/evaluation");
        bean.setRequestMethod(RequestMethod.get);
        bean.addParameter("pageNumber", 0);
        bean.addParameter("pageSize", 2);
        bean.run();
        System.out.println(bean.getResponseContent());
    }

//	@Test
//	public void postTest(){
//		HttpBean bean = new HttpBean("http://139.219.226.16:8443/login");
//		bean.setRequestMethod(RequestMethod.post);
//		bean.addParameter("loginName","admin");
//		bean.addParameter("password","123456");
//		bean.run();
//		System.out.println(bean.getResponseContent());
//		HttpBean bean2 = new HttpBean("http://139.219.226.16:8443/organizationUser/show",RequestMethod.get);
//		bean2.setRequestHeaders(bean.getResponseHeaders());
//		bean2.run();
//		System.out.println(bean2.getResponseContent());
//	}



}
