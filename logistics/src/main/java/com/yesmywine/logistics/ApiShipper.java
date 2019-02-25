package com.yesmywine.logistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by wangdiandian on 2017/4/6.
 */
@SpringBootApplication
@EnableScheduling
public class ApiShipper {
    public static void main(String[] args) {
        SpringApplication.run(ApiShipper.class, args);
    }
//    public class ApiShipper implements EmbeddedServletContainerCustomizer {
//        public static void main(String[] args) {
//            SpringApplication.run(ApiShipper.class,args);
//        }
//        @Override
//        public void customize(ConfigurableEmbeddedServletContainer container) {
//            container.setPort(8443);
//        }
}

