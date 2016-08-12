package org.apel.gaia.persist.config.hibernate;

import javax.persistence.spi.PersistenceUnitTransactionType;

import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

/**
 * 重写JPA unit单元，指定名称
 * @author lijian
 *
 */
public class PlatformPersistenceUnitPostProcessor implements
		PersistenceUnitPostProcessor {

	private String persistenceUnitName;
	private static PersistenceUnitTransactionType defaultTransactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;
	PersistenceUnitTransactionType transactionType = defaultTransactionType;

	public PlatformPersistenceUnitPostProcessor() {
	}

	public PlatformPersistenceUnitPostProcessor(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}
	
	public PlatformPersistenceUnitPostProcessor(String persistenceUnitName,
			PersistenceUnitTransactionType transactionType) {
		this.persistenceUnitName = persistenceUnitName;
		this.transactionType = transactionType;
	}

	public PersistenceUnitTransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(
			PersistenceUnitTransactionType transactionType) {
		this.transactionType = transactionType;
	}


	public String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	public void setPersistenceUnitName(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}

	@Override
	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
		if (persistenceUnitName == null) {
			throw new RuntimeException("persistenceUnitName is not empty!");
		}
		pui.setPersistenceUnitName(persistenceUnitName);
		pui.setTransactionType(transactionType);
	}

}
