package org.apel.gaia.storage.config.db;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SpringDaoConfig {

	@Bean
	public JdbcTemplate jdbcTemlate(DataSource dataSource){
		return new JdbcTemplate(dataSource);
	}
	
}
