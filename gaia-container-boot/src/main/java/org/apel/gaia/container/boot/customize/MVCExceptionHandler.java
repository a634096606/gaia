package org.apel.gaia.container.boot.customize;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apel.gaia.commons.i18n.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author lijian
 * 定义MVC的异常处理，当MVC出现异常时候，此类中的方法进行捕获并进行处理
 */
@Component
public class MVCExceptionHandler implements HandlerExceptionResolver {
	ObjectMapper objectMapper = new ObjectMapper();
	
	private static final String RESPONSE_TIMEOUT = "response timeout";
	private static final String REFERENCE_ERROR1 = "org.springframework.dao.DataIntegrityViolationException";
	private static final String REFERENCE_ERROR2 = "org.hibernate.exception.ConstraintViolationException";
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		try {
			String type = request.getHeader("X-Requested-With");
			String msg = ex.getMessage();
			if(type == null){
				request.setAttribute("error", msg);
				request.getRequestDispatcher("/500").forward(request, response);
			}else{
				msg = msg == null ? "" : msg;
				if(msg.contains(RESPONSE_TIMEOUT)){
					msg = "调用超时，请重试";
				}else if(msg.contains(REFERENCE_ERROR1)
						|| msg.contains(REFERENCE_ERROR2)){
					msg = "数据被引用，不能删除";
				}
				sendError(response, msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
 		}  
		return null;
	}

	private void sendError(HttpServletResponse response, String msg)
			throws JsonProcessingException, IOException {
		Message message = new Message(500, msg);
		String info = objectMapper.writeValueAsString(message);
		PrintWriter writer = response.getWriter();  
		writer.write(info); 
		writer.flush();
		writer.close();
	}

}
