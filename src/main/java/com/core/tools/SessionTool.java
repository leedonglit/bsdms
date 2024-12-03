package com.core.tools;

public class SessionTool {

	
	public static void setSession(String key,Object value) {
		SpringContextTool.getServletRequestAttributes().getRequest().getSession().setAttribute(key, value);
	}
	
	
	public static Object getSession(String key) {
		return SpringContextTool.getServletRequestAttributes().getRequest().getSession().getAttribute(key);
	}
}
