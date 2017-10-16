package org.apel.gaia.app.boot;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * @author lijian
 * 应用启动器--包含security的配置
 *
 */
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class})
@ImportResource("classpath*:META-INF/spring/module-*.xml")
public class AppStarter {
	
	private static final Logger LOG = LoggerFactory.getLogger(AppStarter.class);
	
	public static ApplicationContext start(String... args){
		ConfigurableApplicationContext context = SpringApplication.run(AppStarter.class, args);
		LOG.info("应用服务已成功启动!");
		return context;
	}
	
	public static void startByBlocking(ContextStartedCallback contextStartedCallback, String... args) throws Exception{
		CountDownLatch cdl = new CountDownLatch(1);
		ConfigurableApplicationContext context = SpringApplication.run(AppStarter.class, args);
		if (contextStartedCallback != null) {
			contextStartedCallback.execute(context);
		}
		LOG.info("应用服务已成功启动!");
		cdl.await();
	}
	
	public static void startByBlocking(String... args) throws Exception{
		startByBlocking(null, args);
	}
	
	public interface ContextStartedCallback{
		void execute(ConfigurableApplicationContext context);
	}
	
}
