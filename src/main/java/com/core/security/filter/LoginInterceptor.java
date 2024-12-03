package com.core.security.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.core.security.page.JsonResult;
import com.core.tools.AppUserTool;

import cn.hutool.json.JSONUtil;

public class LoginInterceptor implements HandlerInterceptor {
	/***
	 * 在请求处理之前进行调用(Controller方法调用之前)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (AppUserTool.isLogin(request.getSession().getId())) {//表示当前用户已经登录系统
			return true;
		}else {
			String isAjax = request.getParameter("isAjax");
			if (null != request && "true".equals(isAjax)) {//表示ajax请求
				JsonResult jsonResult = new JsonResult();
				jsonResult.sessiontimeout = true;
				jsonResult.msg = "No login!";
				jsonResult.data = request.getServletContext().getContextPath() + "/login";
				response.setContentType("text/html;charset=utf-8");
				PrintWriter writer = null;
				try {
					writer = response.getWriter();
				} catch (IOException e) {
					e.printStackTrace();
				}
				writer.print(JSONUtil.toJsonStr(jsonResult));
				writer.flush();
				writer.close();
			}else {
				response.sendRedirect(request.getContextPath() + "/login");
			}
		}
		return false;
		//如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
		//如果设置为true时，请求将会继续执行后面的操作
	}

	/***
	 * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

//		System.out.println("执行了拦截器的postHandle方法");
	}

	/***
	 * 整个请求结束之后被调用，也就是在DispatchServlet渲染了对应的视图之后执行（主要用于进行资源清理工作）
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//		System.out.println("执行了拦截器的afterCompletion方法");
	}

}
