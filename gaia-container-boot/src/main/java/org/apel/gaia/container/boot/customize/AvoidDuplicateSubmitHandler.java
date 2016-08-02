package org.apel.gaia.container.boot.customize;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apel.gaia.container.boot.customize.annotation.AvoidDuplicateSubmit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * 避免页面二次提交的拦截器
 * @author lijian
 *
 */
public class AvoidDuplicateSubmitHandler implements HandlerInterceptor{

	private final Logger LOG = LoggerFactory.getLogger(AvoidDuplicateSubmitHandler.class);
	
	private final static String AVOID_DUP_SUBMIT_TOKEN = "avoidDupToken";

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//controller处理之前
		if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			AvoidDuplicateSubmit avoidDuplicateSubmitAnnotation = handlerMethod.getMethodAnnotation(AvoidDuplicateSubmit.class);
			if(avoidDuplicateSubmitAnnotation != null){
				if(avoidDuplicateSubmitAnnotation.putToken()){
					request.getSession().setAttribute(AVOID_DUP_SUBMIT_TOKEN, UUID.randomUUID().toString());
				}
				synchronized (getClass()) {
					if(avoidDuplicateSubmitAnnotation.checkToken()){
						if(isRepeatSubmit(request)){
							 if (isRepeatSubmit(request)) {
			                        LOG.warn("方法 = [" + handlerMethod.getMethod().getName() + "] 发生重复提交");
			                        return false;
			                    }
			                    request.getSession().removeAttribute(AVOID_DUP_SUBMIT_TOKEN);
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession().getAttribute(AVOID_DUP_SUBMIT_TOKEN);
        if (serverToken == null) {
            return true;
        }
        String clinetToken = request.getParameter(AVOID_DUP_SUBMIT_TOKEN);
        if (clinetToken == null) {
            return true;
        }
        if (!serverToken.equals(clinetToken)) {
            return true;
        }
        return false;
    }


	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//controller处理过程中
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//controller处理完成后
	}

}
