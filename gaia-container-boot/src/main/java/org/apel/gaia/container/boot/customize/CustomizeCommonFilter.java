package org.apel.gaia.container.boot.customize;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class CustomizeCommonFilter implements ServletContextInitializer{

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		
		//增加系统容器公用filter
        EnumSet<DispatcherType> dispatcherTypes = EnumSet
				.of(DispatcherType.REQUEST);
        
        //字符集UTF-8 filter
        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("CharacterEncodingFilter", new CharacterEncodingFilter());
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");
        characterEncodingFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
        
        //spring rest http方法隐藏参数支持
        FilterRegistration.Dynamic hiddenHttpMethodFilter = servletContext.addFilter("HiddenHttpMethodFilter", new HiddenHttpMethodFilter());
        hiddenHttpMethodFilter.addMappingForServletNames(dispatcherTypes, true, "mvc");
	}

}
