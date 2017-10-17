package org.apel.gaia.persist.config.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HibernatePropertiesConfig {
	
	@Value("${hibernate.show_sql:true}")
	private String show_sql;
	@Value("${hibernate.format_sql:true}")
	private String format_sql;
	@Value("${hibernate.hbm2ddl_auto:update}")
	private String hbm2ddl_auto = "update";
	@Value("${hibernate.dialect_resolvers:org.apel.gaia.persist.config.hibernate.ExtendDialectResolver}")
	private String dialect_resolvers;
	@Value("${hibernate.use_query_cache:true}")
	private String use_query_cache;
	@Value("${hibernate.use_second_level_cache:true}")
	private String use_second_level_cache;
	@Value("${hibernate.cache_region_factory:org.hibernate.cache.ehcache.EhCacheRegionFactory}")
	private String cache_region_factory;
	
	public Map<String, String> hibernateProperties(){
		Map<String, String> props = new HashMap<String, String>();
		props.put(AvailableSettings.SHOW_SQL, show_sql);
		props.put(AvailableSettings.FORMAT_SQL, format_sql);
		props.put(AvailableSettings.HBM2DDL_AUTO, hbm2ddl_auto);
		props.put(AvailableSettings.DIALECT_RESOLVERS, dialect_resolvers);
		props.put(AvailableSettings.USE_QUERY_CACHE, use_query_cache);
		props.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, use_second_level_cache);
		//hibernate4缓存设置
		props.put(AvailableSettings.CACHE_REGION_FACTORY, cache_region_factory);
		return props;
	}

}
