package com.yesmywine.sso;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

/**
 * Created by SJQ on 12/1/16
 *
 */


@SpringBootApplication
@EnableScheduling
public class ApiuserLogin implements EmbeddedServletContainerCustomizer {
    public static void main(String[] args) {
        SpringApplication.run(ApiuserLogin.class,args);
    }
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8080);
    }

	@Bean
	public DefaultKaptcha captchaProducer(){
		DefaultKaptcha captchaProducer =new DefaultKaptcha();
		Properties properties =new Properties();
		properties.setProperty("kaptcha.border","yes");
		properties.setProperty("kaptcha.brder.color","105,179,90");
		properties.setProperty("kaptcha.textproducer.font.color","blue");
		properties.setProperty("kaptcha.image.width","152");
		properties.setProperty("kaptcha.image.height","45");
		properties.setProperty("kaptcha.textproducer.font.size","36");
		properties.setProperty("kaptcha.session.key","code");
		properties.setProperty("kaptcha.textproducer.char.length","4");
		properties.setProperty("kaptcha.textproducer.font.names","宋体,楷体,微软雅黑");
		Config config=new Config(properties);
		captchaProducer.setConfig(config);
		return  captchaProducer;
	}
}