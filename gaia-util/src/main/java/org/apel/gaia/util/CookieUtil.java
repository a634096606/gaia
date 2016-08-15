package org.apel.gaia.util;

import javax.servlet.http.Cookie;

public class CookieUtil {
	
	public static String getProperty(Cookie[] cookies, String name){
		if(cookies == null) return null;
		for (Cookie cookie : cookies) {
			if(cookie.getName().equals(name)){
				return cookie.getValue();
			}
		}
		return null;
	}
	
	public static boolean containsPropery(Cookie[] cookies, String name){
		if(cookies == null) return false;
		for (Cookie cookie : cookies) {
			if(cookie.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
}
