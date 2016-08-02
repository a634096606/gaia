package org.apel.gaia.container.boot.freemarker;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.ext.jsp.TaglibFactory.ClasspathMetaInfTldSource;

@Configuration
@AutoConfigureAfter(FreeMarkerAutoConfiguration.class)
public class FreeMarkerConfigurerPropertySet {
	
	//让freemarker加载classpath下的所有tld，用于使用jsptaglibs
	@Autowired(required = false)
	public void freeMarkerConfigurerPropertySet(FreeMarkerConfigurer freeMarkerConfigurer, ServletContext sc){
		List<ClasspathMetaInfTldSource> list = new ArrayList<>();
		list.add(new ClasspathMetaInfTldSource(Pattern.compile(".*")));
		if(freeMarkerConfigurer.getTaglibFactory() == null){
			freeMarkerConfigurer.setServletContext(sc);
		}
		freeMarkerConfigurer.getTaglibFactory().setMetaInfTldSources(list);
	}
	
}
