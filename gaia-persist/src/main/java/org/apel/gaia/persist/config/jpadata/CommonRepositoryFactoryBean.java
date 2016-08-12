package org.apel.gaia.persist.config.jpadata;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.apel.gaia.persist.dao.CommonRepositoryImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * @author lijian
 *  
 * 扩展jpaRepository,让所有的repository共享起自定义的方法 
 */
public class CommonRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, I> {

	protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
		return new CommonRepositoryFactory(em);
	}

	private static class CommonRepositoryFactory<T, I extends Serializable>
			extends JpaRepositoryFactory {

		private final EntityManager em;

		public CommonRepositoryFactory(EntityManager em) {
			super(em);
			this.em = em;
		}

		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new CommonRepositoryImpl<T, I>(
					(Class<T>) metadata.getDomainType(), em);
		}

		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return CommonRepositoryImpl.class;
		}
	}

}
