package org.apel.gaia.persist.config.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.cfg.AvailableSettings;

public class HibernatePropertiesConfig {
	private static final String TRUE = "true";
	
	public static Map<String, String> hibernateProperties(){
		Map<String, String> props = new HashMap<String, String>();
		props.put(AvailableSettings.SHOW_SQL, TRUE);
		props.put(AvailableSettings.FORMAT_SQL, TRUE);
		props.put(AvailableSettings.HBM2DDL_AUTO, "update");
		props.put(AvailableSettings.DIALECT_RESOLVERS, "org.apel.gaia.persist.config.hibernate.ExtendDialectResolver");
		props.put(AvailableSettings.USE_QUERY_CACHE, TRUE);
		props.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, TRUE);
		//hibernate4缓存设置
		props.put(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.EhCacheRegionFactory");
		return props;
	}

}
