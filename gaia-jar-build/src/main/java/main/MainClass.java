package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class })
@ImportResource("classpath*:META-INF/spring/module-*.xml")
@EnableEurekaClient
public class MainClass {

	// spring boot启动
	// public static void main(String[] args) throws Exception{
	// TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
	// SimplePlatformStarter.start(args);
	// }

	// //dubbo 服务启动
	// public static void main(String[] args) throws Exception {
	// CountDownLatch cdl = new CountDownLatch(1);
	// ClassPathXmlApplicationContext context = new
	// ClassPathXmlApplicationContext("classpath*:META-INF/spring/module-*.xml");
	// context.start();
	// System.out.println("************服务启动完成");
	// cdl.await();
	// context.close();
	// }

	public static void main(String[] args) {
		SpringApplication.run(MainClass.class, args);
	}

}
