package org.apel.gaia.app.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * @author lijian
 * 系统平台启动器--包含security的配置
 *
 */
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class})
@ImportResource("classpath*:META-INF/spring/module-*.xml")
public class AppStarter {
	
	public static ApplicationContext start(String... args){
		 return SpringApplication.run(AppStarter.class, args);
	}
	
}
