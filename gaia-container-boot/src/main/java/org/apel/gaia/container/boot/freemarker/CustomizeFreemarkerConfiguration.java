package org.apel.gaia.container.boot.freemarker;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apel.gaia.util.GlobalAppPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 *
 *改造spring-boot对于freemarker的集成，原始集成freemarker的模板只能扫描当前工程，
 *通过重写方法重新加载所有类路径下的模板，使得整个框架能够扫描所有工程
 *
 */
@Configuration
@AutoConfigureBefore(FreeMarkerAutoConfiguration.class)
public class CustomizeFreemarkerConfiguration extends WebMvcConfigurerAdapter{
	
	private static Logger LOG = LoggerFactory.getLogger(CustomizeFreemarkerConfiguration.class);
	//app properties当中的ftl键
	private static final String FTL = "ftl";
	ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	@Autowired(required = false)
	public void setProperties(FreeMarkerProperties properties) {
		try {
			Resource[] resources = resourcePatternResolver.getResources("classpath*:/platform/templates/");
			String[] templateLoaderPaths = new String[resources.length];
			for (int i = 0; i < resources.length; i++) {
				String templateLoaderPath = resources[i].getURI().toString();
				templateLoaderPaths[i] = templateLoaderPath;
			} 
			String[] oldTemplateLoaderPaths = properties.getTemplateLoaderPath();
			templateLoaderPaths = (String[]) ArrayUtils.addAll(templateLoaderPaths, oldTemplateLoaderPaths);
			properties.setTemplateLoaderPath(templateLoaderPaths);
			String springFtl = "/spring.ftl as spring";
			String allFtl = findAllFtl();
			String ftls = allFtl == null ? springFtl : springFtl + "," + allFtl;
			properties.getSettings().put("auto_import", ftls);
		} catch (Exception e) {
			LOG.error("系统加载模板资源信息失败");
			throw new RuntimeException(e);
		}
	}
	
	//加载系统当中所有定义的宏
	private String findAllFtl(){
		List<Properties> allProperties = GlobalAppPropertiesUtil.findAllAppProperties();
		StringBuffer sb = new StringBuffer();
		for (Properties properties : allProperties) {
			String ftlValue = properties.getProperty(FTL);
			if(ftlValue != null){
				sb.append(ftlValue + ",");
			}
		}
		if(sb.length() == 0){
			return null;
		}else{
			return sb.substring(0, sb.length() - 1);
		}
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LocaleChangeInterceptor());
	}
	
	
}
