package org.apel.gaia.persist.config.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apel.gaia.persist.config.db.DBConfig;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.pagehelper.PageHelper;

/**
 * mybatis配置
 * @author lijian
 *
 */
@Configuration
public class MybatisConfig {
	
	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource){
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);
		return dataSourceTransactionManager;
	}
	
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
		SqlSessionFactoryExtend sqlSessionFactoryBean = new SqlSessionFactoryExtend();
		sqlSessionFactoryBean.setTypeAliasesPackage("**.domain");
		sqlSessionFactoryBean.setDataSource(dataSource);
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		String dialect = "mysql";
		if(!StringUtils.isEmpty(DBConfig.getDialect())){
			dialect = DBConfig.getDialect();
		}
		p.put("dialect", dialect);
		pageHelper.setProperties(p);
		sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});
		return sqlSessionFactoryBean;
	}
	
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(){
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage("**.dao");
		return mapperScannerConfigurer;
	}
	
}
