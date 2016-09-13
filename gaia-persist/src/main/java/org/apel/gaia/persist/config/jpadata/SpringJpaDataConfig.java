package org.apel.gaia.persist.config.jpadata;

import org.apel.gaia.persist.dao.CommonRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.apel.**.dao", 
							repositoryBaseClass = CommonRepositoryImpl.class)
public class SpringJpaDataConfig {
	
	
}
