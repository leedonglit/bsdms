package com.core.security.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class LimitFilter implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//每秒只能接收5个请求
		registry.addInterceptor(new LimitInterceptor(5,LimitInterceptor.LimitType.DROP))
		.addPathPatterns("/**")
		//忽略拦截
		.excludePathPatterns("/**/*.js","/**/*.css","/**/*.png","/**/*.jpg","/**/*.gif");
	}
}
