package org.apel.gaia.container.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * @author lijian
 * 系统平台启动器--不包含security的配置
 *
 */
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class, SecurityAutoConfiguration.class})
@ImportResource("classpath*:META-INF/spring/module-*.xml")
public class SimplePlatformStarter {
	
	public static ApplicationContext start(String... args){
		 return SpringApplication.run(SimplePlatformStarter.class, args);
	}
	
}
