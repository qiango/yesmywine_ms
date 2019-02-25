package com.yesmywine.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

/**
 * Created by WANG, RUIQING on 12/1/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@SpringBootApplication
public class ApiInventory
            implements EmbeddedServletContainerCustomizer

    {

        public static void main(String[] args) {

            SpringApplication.run(ApiInventory.class, args);
    }


        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.setPort(8080);
        }
    }
