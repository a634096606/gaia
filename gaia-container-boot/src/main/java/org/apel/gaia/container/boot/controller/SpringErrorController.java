package org.apel.gaia.container.boot.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * spring特定异常跳转
 * @author lijian
 *
 */
@Controller
public class SpringErrorController implements ErrorController {

	private static final String PATH = "/error";

	/**
	 * 重写spring的异常跳转，例如405异常，请求的方法不支持get请求等，该异常还没有进入到controller
	 * 内部，先由spring进行了拦截，为了自定义错误，重写此方法实现异常跳转 
	 */
	@RequestMapping(value = PATH)
	public String error() {
		return "405";
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

}
