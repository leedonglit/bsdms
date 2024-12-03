package com.core.security.filter;

import cn.hutool.extra.servlet.ServletUtil;
import com.core.tools.BeanTool;
import com.isec.base.monit.service.UserService;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "filter",urlPatterns="/*")
public class SecurityFilter implements Filter{

	private static UserService userService = null;
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request =  (HttpServletRequest) req;
		HttpServletResponse response =  (HttpServletResponse) res;
		req.setAttribute("ctxPath", req.getServletContext().getContextPath());
		if (userService == null) {
			userService = BeanTool.getBean(UserService.class, request);
		}
		String url = request.getRequestURI();
		if (!url.contains(".js") && !url.contains(".css") && !url.contains("/ping") && !url.contains("/images")){
			userService.uptLastLogin("", ServletUtil.getClientIP(request),request.getRequestURI());
		}
		chain.doFilter(request, response);
	}

	public void destroy() {

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
