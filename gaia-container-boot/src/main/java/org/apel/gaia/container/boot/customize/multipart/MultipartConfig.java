package org.apel.gaia.container.boot.customize.multipart;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class MultipartConfig {
	
	@Autowired
	private ApplicationContext context;

	@Bean
	public MultipartResolver multipartResolver(){
		Map<String, CommonsMultipartResolver> commonsMultipartResolvers = context.getBeansOfType(CommonsMultipartResolver.class);
		if(commonsMultipartResolvers.size() > 1){
			throw new RuntimeException("spring容器中找到两个及以上的CommonsMultipartResolver, 请检查配置情况");
		}
		CommonsMultipartResolver resolver = null;
		for (Entry<String, CommonsMultipartResolver> entry : commonsMultipartResolvers.entrySet()) {
			resolver = entry.getValue();
		}
		
		if(resolver == null){
			resolver = new MultipartListenerResolver();
		}
		resolver.setDefaultEncoding("utf-8");
		resolver.setMaxInMemorySize(40960);
		resolver.setMaxUploadSize(10485760000L);
		return resolver;
	}
	
}
