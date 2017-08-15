package org.apel.gaia.storage.config.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.apel.gaia.storage.factory.DataSourceFactory;
import org.apel.gaia.util.encryption.DESUtil;
import org.apel.gaia.util.encryption.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author lijian
 * 从用户使用空间的config文件夹下的db.properties中加载数据源信息
 * 用druid创建系统平台数据源
 * 另外,db.properties中的所有信息采用公钥加密
 *
 */
@Configuration
public class DBConfig {
	
	private Logger logger = LoggerFactory.getLogger(DBConfig.class);

	private static final String CONFIG = "config";

	private static final String PLATFORM_PROPERTIES = "db.properties";

	private static final String USER_DIR = "user.dir";
	
	private char[] urlChars = new char[]{0x6a,0x64,0x62,0x63,0x2e,0x75,0x72,0x6c};
	private char[] driverclassChars = new char[]{0x6a,0x64,0x62,0x63,0x2e,0x64,0x72,0x69,0x76,0x65,0x72,0x63,0x6c,0x61,0x73,0x73};
	private char[] usernameChars = new char[]{0x6a,0x64,0x62,0x63,0x2e,0x75,0x73,0x65,0x72,0x6e,0x61,0x6d,0x65};
	private char[] passwordChars = new char[]{0x6a,0x64,0x62,0x63,0x2e,0x70,0x61,0x73,0x73,0x77,0x6f,0x72,0x64};
	private char[] seed = new char[] { 0x77,0x77,0x77,0x2e,0x6f,0x72,0x67,0x2e,0x61,0x70,0x65,0x6c};
	private char[] xaDsClassNameChars = new char[]{0x6a,0x64,0x62,0x63,0x2e,0x78,0x61,0x44,0x61,0x74,0x61,0x53,0x6f,0x75,0x72,0x63,0x65,0x43,0x6c,0x61,0x73,0x73,0x4e,0x61,0x6d,0x65};

	private static Properties props;
	
	//druid datasource
	private DruidDataSource dataSource;
	
	@Autowired(required = false)
	private DataSourceFactory dataSourceFactory;
	
	static{
		try {
			props = loadDbProperties();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static Properties loadDbProperties() throws Exception{
		Properties props = new Properties();
		File rootDir = new File(System.getProperty(USER_DIR));
		File platformFile = new File(rootDir,CONFIG + File.separator + PLATFORM_PROPERTIES);
		if(!platformFile.exists()){
			platformFile.getParentFile().mkdirs();
			Properties prop = new Properties();
			prop.setProperty("test", "true");
			FileOutputStream fos = null;
			fos = new FileOutputStream(platformFile);
			prop.store(fos, "Copyright (c) http://www.apel.org");
			fos.close();
		}
		try(InputStream inStream  = new FileInputStream(platformFile);){
			props.load(inStream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return props;
	}
	
	
	/**
	 * 配置druid datasource
	 */
	@Bean
	public DataSource dataSource() throws Exception{
		if (dataSourceFactory != null){
			return dataSourceFactory.createDataSource();
		}
		buildDs(props);
		return dataSource;
	}
	
	public static String getDialect(){
		return props.getProperty("dialect"); 
	}
	
	private String encryption(char[] content) throws Exception {
		String result = MD5Util.encodeMD5Hex(new String(content));
		return Base64.encodeBase64String(result.getBytes()).replaceAll(
				new String(new char[] { 0x3d }),
				new String(new char[] { 0x4e }));
	}

	private void buildDs(Properties props) throws Exception{
		//如果不存在db.properties文件或者是配置文件中test属性值为true则
		//创建h2测试数据库
		String test = props.getProperty("test");
		if(test!=null && test.equals("true")){
			buildH2Ds(props);
			return;
		}
		String url  = props.getProperty(encryption(urlChars));
		String driverClass = props.getProperty(encryption(driverclassChars));
		String username = props.getProperty(encryption(usernameChars));
		String password = props.getProperty(encryption(passwordChars));
		String xaDsClassName = props.getProperty(encryption(xaDsClassNameChars));
		byte[] key = DESUtil.initKey(new String(seed));
		
		url = new String(DESUtil.decrypt(Base64.decodeBase64(url), key)).substring(32);
		driverClass = new String(DESUtil.decrypt(Base64.decodeBase64(driverClass), key)).substring(32);
		username = new String(DESUtil.decrypt(Base64.decodeBase64(username), key)).substring(32);
		password = new String(DESUtil.decrypt(Base64.decodeBase64(password), key)).substring(32);
		xaDsClassName = new String(DESUtil.decrypt(Base64.decodeBase64(xaDsClassName), key)).substring(32);
		
		buildDataSource(props, new String(Base64.decodeBase64(url.getBytes())),
				new String(Base64.decodeBase64(username.getBytes())),
				new String(Base64.decodeBase64(password.getBytes())));
	}


	private void buildDataSource(Properties props, String url, String username,
			String password) throws SQLException {
		dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		DruidAssembler druidProperties = new DruidAssembler(props);
		druidProperties.assembleDruid(dataSource);
	}

	/**
	 * 构建测试数据库
	 * @throws Exception 
	 */
	private void buildH2Ds(Properties props) throws Exception {
		System.out.println("构建H2测试数据库");
		logger.info("构建H2测试数据库");
		File  rootDir = new File(System.getProperty(USER_DIR));
		File dataDir = new File(rootDir,"data");
		dataDir.mkdirs();
		String url ="jdbc:h2:" +  dataDir + File.separator + "platform;AUTO_SERVER=TRUE";
		String username  = "platform";
		String password = "123";
		buildDataSource(props, url, username, password);
	}

	
	
	
}
