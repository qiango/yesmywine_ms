package com.yesmywine.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

/**
 * Created by WANG, RUIQING on 11/30/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@SpringBootApplication
public class ApiPay   implements EmbeddedServletContainerCustomizer {

        public static void main(String[] args) {

        SpringApplication.run(ApiPay.class, args);
    }


    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8080);
    }
}
