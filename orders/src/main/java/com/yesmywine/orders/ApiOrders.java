package com.yesmywine.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by wangdiandian on 2017/4/7.
 */
@SpringBootApplication
@EnableScheduling
public class ApiOrders {
    public static void main(String[] args) {
        SpringApplication.run(ApiOrders.class, args);
    }

}

//public class ApiOrders implements EmbeddedServletContainerCustomizer {
//    public static void main(String[] args) {
//        SpringApplication.run(ApiOrders.class, args);
//    }
//        @Override
//    public void customize(ConfigurableEmbeddedServletContainer container) {
//        container.setPort(8443);
//    }
