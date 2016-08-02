package org.apel.gaia.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class GlobalAppPropertiesUtil {

	//加载所有jar包类路径下面的config/app.properties
	private static String APP_PROPERTIES = "config/app.properties";
	
	private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private static List<Properties> propertiesList = new ArrayList<>();
	
	static{
		try {
			Resource[] xmlResources = resourcePatternResolver.getResources("classpath*:" + APP_PROPERTIES);
			for (Resource resource : xmlResources) {
				Properties propertiesFile = new Properties();
				InputStream is = resource.getInputStream();
				propertiesFile.load(is);
				is.close();
				propertiesList.add(propertiesFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Properties> findAllAppProperties(){
		return propertiesList;
	}
	
	//随机拿到第一个，该方法一般很少使用，除非确定当前的key值是全系统唯一的
	public static String getAppProperty(String key){
		for (Properties properties : propertiesList) {
			if(properties.getProperty(key) != null){
				return properties.getProperty(key);
			}
		}
		return null;
	}
}
