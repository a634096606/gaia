package org.apel.gaia.dubbo.extend.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.util.StringUtils;

/**
 * 在dubbo扩展点中的ip过滤对象
 * 
 * @author lijian
 *
 */
public class IP {

	private static final String WHITE_IPS = "white_ips";
	private static final String ENABLED = "enabled";

	private IP() {
	}

	private List<String> whiteIps = new ArrayList<>();

	private boolean enabled;

	public List<String> getWhiteIps() {
		return whiteIps;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public static IP buildIP(Properties prop) {
		IP ip = new IP();
		if (!StringUtils.isEmpty(prop.getProperty(WHITE_IPS))) {
			ip.whiteIps = Arrays.asList(prop.getProperty(WHITE_IPS).split(","));
		}
		if(!StringUtils.isEmpty(prop.getProperty(ENABLED))){
			if("true".equals(prop.getProperty(ENABLED))){
				ip.enabled = true;
			}else{
				ip.enabled = false;
			}
		}
		return ip;
	}

}
