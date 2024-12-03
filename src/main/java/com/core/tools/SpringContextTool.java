package com.core.tools;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SpringContextTool {


	public static ApplicationContext context = null;

	public static void setContext(ApplicationContext context1) {
		context = context1;
	}

	public static ServletRequestAttributes getServletRequestAttributes() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attributes;
	}
 
}
