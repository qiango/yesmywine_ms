package com.yesmywine.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Created by WANG, RUIQING on 12/1/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@SpringBootApplication
@EnableScheduling
public class Apiuser{
//        implements EmbeddedServletContainerCustomizer {
    public static void main(String[] args) {
        SpringApplication.run(Apiuser.class, args);

    }

    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8080);
    }
//public class Apiuser implements EmbeddedServletContainerCustomizer {
//    public static void main(String[] args) {
//        SpringApplication.run(Apiuser.class, args);
//    }
//
//    @Override
//    public void customize(ConfigurableEmbeddedServletContainer container) {
//        container.setPort(8443);
//    }
}

