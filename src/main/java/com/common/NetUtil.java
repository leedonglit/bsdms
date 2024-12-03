package com.common;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

public class NetUtil {

	
	/**
	 * 判断字符串是否是url地址
	 * @param URLName
	 * @return
	 */
	public static boolean existsURL(String URLName) {
		try {
			//设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
			HttpURLConnection.setFollowRedirects(false);
			//到 URL 所引用的远程对象的连接
			HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
			/* 设置 URL 请求的方法， GET POST HEAD OPTIONS PUT DELETE TRACE 以上方法之一是合法的，具体取决于协议的限制。*/
			con.setRequestMethod("HEAD");
			//从 HTTP 响应消息获取状态码
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 获取用户访问IP
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {  
		String ip = request.getHeader("x-forwarded-for");  
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("Proxy-Client-IP");  
		}  
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("WL-Proxy-Client-IP");  
		}  
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getRemoteAddr();  
		}  
		return ip;  
	}  
	
	/**
	 * 判断是否是ajax请求
	 * @param request
	 * @return
	 */
	public static String getRequestType(HttpServletRequest request) {

		return request.getHeader("X-Requested-With");
	}
	
	//获取请求浏览器类型
	public static String getBrowserType(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}
}
