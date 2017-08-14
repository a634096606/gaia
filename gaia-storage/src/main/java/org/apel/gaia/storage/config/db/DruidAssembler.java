package org.apel.gaia.storage.config.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.util.StringUtils;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.MergeStatFilter;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * druid 属性装备器
 * @author lijian
 *
 */
public class DruidAssembler {
	
	private static final int DEFAULT_INITIAL_SIZE = 1;
	private static final int DEFAULT_MIN_IDLE = 1;
	private static final int DEFAULT_MAX_ACTIVE = 100;
	private static final int DEFAULT_MAX_WAIT = 60000;
	private static final int DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 60000;
	private static final int DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 300000;
	private static final String DEFAULT_VALIDATION_QUERY = "SELECT 'x'";
	private static final boolean DEFAULT_TEST_WHILE_IDLE = true;
	private static final boolean DEFAULT_TEST_ON_BORROW = false;
	private static final boolean DEFAULT_TEST_ON_RETURN = false;
	private static final boolean DEFAULT_POOL_PREPARED_STATEMENTS = false;
	private static final int DEFAULT_MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE = 20;
	
	private static final String INITIAL_SIZE = "initialSize";
	private static final String MIN_IDLE = "minIdle";
	private static final String MAX_ACTIVE = "maxActive";
	private static final String MAX_WAIT = "maxWait";
	private static final String TIME_BETWEEN_EVICTION_RUNS_MILLIS = "timeBetweenEvictionRunsMillis";
	private static final String MIN_EVICTABLE_IDLE_TIME_MILLIS = "minEvictableIdleTimeMillis";
	private static final String VALIDATION_QUERY = "validationQuery";
	private static final String TEST_WHILE_IDLE = "testWhileIdle";
	private static final String TEST_ON_BORROW = "testOnBorrow";
	private static final String TEST_ON_RETURN = "testOnReturn";
	private static final String POOL_PREPARED_STATEMENTS = "poolPreparedStatements";
	private static final String MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE = "maxPoolPreparedStatementPerConnectionSize";
	
	private Properties props;
	
	public DruidAssembler(Properties props){
		this.props = props;
	}
	
	public void assembleDruid(DruidDataSource dataSource) throws SQLException{
		int initialSize = 
				StringUtils.isEmpty(props.getProperty(INITIAL_SIZE)) ? 
						DEFAULT_INITIAL_SIZE : Integer.valueOf(props.getProperty(INITIAL_SIZE));
		int minIdle = 
				StringUtils.isEmpty(props.getProperty(MIN_IDLE)) ? 
						DEFAULT_MIN_IDLE : Integer.valueOf(props.getProperty(MIN_IDLE));
		int maxActive = 
				StringUtils.isEmpty(props.getProperty(MAX_ACTIVE)) ? 
						DEFAULT_MAX_ACTIVE : Integer.valueOf(props.getProperty(MAX_ACTIVE));
		int maxWait = 
				StringUtils.isEmpty(props.getProperty(MAX_WAIT)) ? 
						DEFAULT_MAX_WAIT : Integer.valueOf(props.getProperty(MAX_WAIT));
		int timeBetweenEvictionRunsMillis = 
				StringUtils.isEmpty(props.getProperty(TIME_BETWEEN_EVICTION_RUNS_MILLIS)) ? 
						DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS : Integer.valueOf(props.getProperty(TIME_BETWEEN_EVICTION_RUNS_MILLIS));
		int minEvictableIdleTimeMillis = 
				StringUtils.isEmpty(props.getProperty(MIN_EVICTABLE_IDLE_TIME_MILLIS)) ? 
						DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS : Integer.valueOf(props.getProperty(MIN_EVICTABLE_IDLE_TIME_MILLIS));
		String validationQuery = 
				StringUtils.isEmpty(props.getProperty(VALIDATION_QUERY)) ? 
						DEFAULT_VALIDATION_QUERY : props.getProperty(VALIDATION_QUERY);
		boolean testWhileIdle = 
				StringUtils.isEmpty(props.getProperty(TEST_WHILE_IDLE)) ? 
						DEFAULT_TEST_WHILE_IDLE : Boolean.valueOf(props.getProperty(TEST_WHILE_IDLE));
		boolean testOnBorrow = 
				StringUtils.isEmpty(props.getProperty(TEST_ON_BORROW)) ? 
						DEFAULT_TEST_ON_BORROW : Boolean.valueOf(props.getProperty(TEST_ON_BORROW));
		boolean testOnReturn = 
				StringUtils.isEmpty(props.getProperty(TEST_ON_RETURN)) ? 
						DEFAULT_TEST_ON_RETURN : Boolean.valueOf(props.getProperty(TEST_ON_RETURN));
		boolean poolPreparedStatements = 
				StringUtils.isEmpty(props.getProperty(POOL_PREPARED_STATEMENTS)) ? 
						DEFAULT_POOL_PREPARED_STATEMENTS : Boolean.valueOf(props.getProperty(POOL_PREPARED_STATEMENTS));
		int maxPoolPreparedStatementPerConnectionSize = 
				StringUtils.isEmpty(props.getProperty(MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE)) ? 
						DEFAULT_MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE : Integer.valueOf(props.getProperty(MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE));
		
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxWait(maxWait);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		//配置druid filter
		Slf4jLogFilter logFilter = new Slf4jLogFilter();
		MergeStatFilter  statFilter = new MergeStatFilter();
		//记录慢sql
		statFilter.setSlowSqlMillis(10000);
		statFilter.setLogSlowSql(true);
		logFilter.setStatementExecutableSqlLogEnable(true);
		List<Filter> filters = new ArrayList<>();
		filters.add(logFilter);
		filters.add(statFilter);
		dataSource.setProxyFilters(filters);
		//定时5分钟将监控数据输出到日志文件中
		dataSource.setTimeBetweenLogStatsMillis(300000);
	}
	
	
	
}
