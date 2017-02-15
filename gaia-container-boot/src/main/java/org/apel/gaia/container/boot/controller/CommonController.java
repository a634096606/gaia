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
		URL urlObject = null;
		HttpURLConnection conn = null;
		try {
			urlObject = new URL(url);
			conn = (HttpURLConnection)urlObject.openConnection();
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(2000);
			conn.getResponseCode();
		} catch (Exception e) {
			result = "error";
		} finally{
			if(conn != null){
				conn.disconnect();
			}
		}
		return result;
	}

}
