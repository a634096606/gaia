package org.apel.gaia.container.boot.controller;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台公共controller
 * @author lijian
 *
 */
@RestController
@RequestMapping("/")
public class CommonController {

	@RequestMapping("net_detect")
	public @ResponseBody String netDedect(){
		return "ok";
	}
	
	@RequestMapping("net_check")
	public @ResponseBody String netCheck(String url){
		String result = "ok";
		try {
			URL urlObject = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)urlObject.openConnection();
			conn.setConnectTimeout(200);
			conn.getResponseCode();
		} catch (Exception e) {
			result = "error";
		} 
		return result;
	}

}
