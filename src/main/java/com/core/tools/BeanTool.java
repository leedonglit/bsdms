package com.core.tools;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BeanTool {

	public static <T> T getBean(Class<T> clazz,HttpServletRequest request) {
		BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
		return factory.getBean(clazz);
	}
}
