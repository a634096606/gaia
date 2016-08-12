package org.apel.gaia.persist.config.hibernate;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TxConfig {
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Bean
	public PlatformTransactionManager transactionManager(){
	   PlatformTransactionManager transactionManager =	new JpaTransactionManager(entityManagerFactory);
	   return transactionManager;
	}
	

}
