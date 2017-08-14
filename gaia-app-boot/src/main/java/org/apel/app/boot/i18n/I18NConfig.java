package org.apel.app.boot.i18n;

import org.apel.gaia.commons.i18n.MessageUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class I18NConfig {
	@Bean
	public MessageSource messageSource() {
		return MessageUtil.getMessageSource();
	}
	
	@Bean
	public LocaleResolver localeResolver(){
		return new SessionLocaleResolver();
	}
}
