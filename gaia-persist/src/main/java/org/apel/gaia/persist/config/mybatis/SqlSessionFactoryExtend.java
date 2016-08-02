package org.apel.gaia.persist.config.mybatis;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 扩展mybatis sqlsessionfacoty
 * 让其typeAlias支持正则表达式中的通配符
 * @author lijian
 *
 */
public class SqlSessionFactoryExtend extends SqlSessionFactoryBean{

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlSessionFactoryExtend.class);
	
	//通过通配符扫描出来的package集合
	private Set<String> packageSet = new LinkedHashSet<>();
	//类路径jar标识
	private static final String JAR_IDENTIFIER = "jar!";
	//类路径本地文件系统标识
	private static final String FILE_IDENTIFIER = "target/classes";
	//默认的mybatis model路径
	private static final String MYBATIS_MODEL = "domain";
	
	@Override
    public void setTypeAliasesPackage(String typeAliasesPackage) {
        if (StringUtils.isBlank(typeAliasesPackage)) {
            super.setTypeAliasesPackage(typeAliasesPackage);
        }
        if(typeAliasesPackage.contains("*") || typeAliasesPackage.contains("**")){
        	LOGGER.info("mybatis扫描Model全局路径:" + typeAliasesPackage);
        	String scanGlobalPackages = findGlobalPackages(typeAliasesPackage);
        	if(!StringUtils.isEmpty(scanGlobalPackages)){
        		LOGGER.info("mybatis自动扫描Model的所有结果:" + scanGlobalPackages);
        		super.setTypeAliasesPackage(scanGlobalPackages);
        	}
        }
    }
	
	/**
	 * 查找当前类路径下所有domain包
	 */
	private String findGlobalPackages(String typeAliasesPackage) {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources;
		try {
			resources = resourcePatternResolver.getResources("classpath*:**/domain/**");
			for (Resource resource : resources) {
				String url = resource.getURL().toString();
				if(url.startsWith("file:")){
					url = url.substring(url.indexOf(FILE_IDENTIFIER) + FILE_IDENTIFIER.length() + 1, 
							url.indexOf(MYBATIS_MODEL) + MYBATIS_MODEL.length()).replaceAll("/", ".");
					packageSet.add(url);
				}
				if(url.startsWith("jar:")){
					url = url.substring(url.indexOf(JAR_IDENTIFIER) + JAR_IDENTIFIER.length() + 1, 
							url.indexOf(MYBATIS_MODEL) + MYBATIS_MODEL.length()).replaceAll("/", ".");
					packageSet.add(url);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		String packages = packageSet.size() == 0 ? "" : StringUtils.join(packageSet, ",");
		return packages;
	}

	
	
}
