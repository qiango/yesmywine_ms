package com.yesmywine.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by hz on 12/19/16.
 */
@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration
public class ActivityApi
        implements EmbeddedServletContainerCustomizer {
    public static void main(String[] args) {
        SpringApplication.run(ActivityApi.class, args);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8080);
    }
}


