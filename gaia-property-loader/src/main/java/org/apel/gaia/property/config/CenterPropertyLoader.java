package org.apel.gaia.property.config;

import java.io.File;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
public class CenterPropertyLoader {

	public static final String CENTER_PROPERTY_LOC = "/config/center.properties";
	
	@Bean
	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(){
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		//避免在spring boot启动时 容器顺序打乱，始终让该类先执行，因为很多其他工程的配置都需要依赖这个property解析类
		propertyPlaceholderConfigurer.setOrder(1);
		Resource resource = new FileSystemResource(new File(System.getProperty("user.dir") + CENTER_PROPERTY_LOC));
		propertyPlaceholderConfigurer.setLocation(resource);
		return propertyPlaceholderConfigurer;
	}
	
}
