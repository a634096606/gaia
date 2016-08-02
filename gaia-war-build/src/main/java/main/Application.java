package main;

import org.apel.gaia.container.boot.PlatformStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

public class Application extends SpringBootServletInitializer{
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PlatformStarter.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PlatformStarter.class, args);
    }
	
	
}
