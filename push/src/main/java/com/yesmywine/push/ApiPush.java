package com.yesmywine.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by hz on 12/8/16.
 */
@SpringBootApplication
@EnableScheduling
public class ApiPush {
    public static void main(String[] args) {
        SpringApplication.run(ApiPush.class, args);
    }
}
//public class ApiGoods implements EmbeddedServletContainerCustomizer {
//    public static void main(String[] args) {
//        SpringApplication.run(ApiGoods.class,args);
//    }
//    @Override
//    public void customize(ConfigurableEmbeddedServletContainer container) {
//        container.setPort(8443);
//    }
//}
