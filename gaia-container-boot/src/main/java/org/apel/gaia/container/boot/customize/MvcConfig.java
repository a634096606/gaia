package org.apel.gaia.container.boot.customize;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

/**
 * spring mvc 补充配置
 * @author lijian
 *
 */
@Configuration
public class MvcConfig {
	
	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
	
}
