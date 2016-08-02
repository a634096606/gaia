package org.apel.gaia.container.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 平台异常页面响应
 * @author lijian
 *
 */
@Controller
@RequestMapping(value = "/")
public class ContainerErrorController {
	
	@RequestMapping(value="400")
	public String _400()throws Exception{
		return "400";
	}
	
	@RequestMapping(value="403")
	public String _403()throws Exception{
		return "403";
	}
	
	@RequestMapping(value="404")
	public String _404()throws Exception{
		return "404";
	}
	
	@RequestMapping(value="405")
	public String _405()throws Exception{
		return "405";
	}
	
	@RequestMapping(value="500")
	public String _500()throws Exception{
		return "500";
	}
	
}
