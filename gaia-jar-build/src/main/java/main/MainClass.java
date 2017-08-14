package main;

import java.util.TimeZone;

import org.apel.gaia.container.boot.SimplePlatformStarter;








public class MainClass {
	
	//spring boot启动
	public static void main(String[] args) throws Exception{
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		SimplePlatformStarter.start(args);
	}
	
//	//dubbo 服务启动
//	public static void main(String[] args) throws Exception {
//		CountDownLatch cdl = new CountDownLatch(1);
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/module-*.xml");
//		context.start();
//		System.out.println("************服务启动完成");
//		cdl.await();
//		context.close();
//	}
	
	
	
	
	
}
