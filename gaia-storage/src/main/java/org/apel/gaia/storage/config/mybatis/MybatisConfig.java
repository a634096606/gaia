package org.apel.gaia.storage.config.mybatis;

import java.util.Properties;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tk.mybatis.mapper.entity.Config;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@Configuration
@MapperScan(MybatisConfig.MYBATIS_SCAN_PACKAGE)
public class MybatisConfig {
	
	public static final String MYBATIS_SCAN_PACKAGE = "org.apel.**.dao";

	//通用mapper配置
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(){
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		Config config = new Config();
		mapperScannerConfigurer.getMapperHelper().setConfig(config);
		mapperScannerConfigurer.setBasePackage(MybatisConfig.MYBATIS_SCAN_PACKAGE);
		Properties prop = new Properties();
		prop.put("mappers", "org.apel.gaia.storage.general.CustomPageMapper");
		mapperScannerConfigurer.setProperties(prop);
		return  mapperScannerConfigurer;
	}
	
}
