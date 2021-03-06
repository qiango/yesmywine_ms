package com.yesmywine.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by hz on 12/8/16.
 */
@SpringBootApplication
@EnableScheduling
//public class ApiFileUpload {
//    public static void main(String[] args) {
//        SpringApplication.run(ApiFileUpload.class, args);
//    }
//}
public class ApiFileUpload implements EmbeddedServletContainerCustomizer {
    public static void main(String[] args) {
        SpringApplication.run(ApiFileUpload.class,args);
    }
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8080);
    }
}
