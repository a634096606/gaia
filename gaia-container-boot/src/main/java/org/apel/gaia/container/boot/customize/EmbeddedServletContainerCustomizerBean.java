package org.apel.gaia.container.boot.customize;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class EmbeddedServletContainerCustomizerBean implements
		EmbeddedServletContainerCustomizer {

	/* 
	 * 定义容器错误400,404,500
	 */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400"));
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
		container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
		container.setSessionTimeout((int)TimeUnit.HOURS.toSeconds(8));//session八小时过期
		//设置jetty post form提交数据大小
		if (container instanceof JettyEmbeddedServletContainerFactory) {
          JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory=(JettyEmbeddedServletContainerFactory) container;
          jettyEmbeddedServletContainerFactory.addServerCustomizers(new JettyServerCustomizer(){
				@Override
				public void customize(Server server) {
					server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", -1);
				}
          });
		}

	} 
}
