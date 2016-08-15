package org.apel.gaia.container.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author lijian
 * 系统平台启动器
 *
 */
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class})
@ImportResource("classpath*:META-INF/spring/module-*.xml")
public class PlatformStarter {
	
	public static void start(String... args){
		 SpringApplication.run(PlatformStarter.class, args);
	}
	
}
