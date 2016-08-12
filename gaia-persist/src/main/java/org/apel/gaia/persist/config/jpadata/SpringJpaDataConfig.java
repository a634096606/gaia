package org.apel.gaia.persist.config.jpadata;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableJpaRepositories(basePackages = "org.apel.**.dao", 
							repositoryFactoryBeanClass = CommonRepositoryFactoryBean.class)
@EnableSpringDataWebSupport
public class SpringJpaDataConfig {
	
	
}
