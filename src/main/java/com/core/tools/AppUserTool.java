package com.core.tools;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AppUserTool extends FinalVarStaticTool {

	public static Map<String, AppUser> sessions = new HashMap<>();

	//当前在线人数
	public static Integer online = 0;

	public static String getSessionId() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attributes.getRequest().getSession().getId();
	}

	/**
	 * 获取SESSION用户信息
	 * @return
	 */
	public static synchronized AppUser getAppUser()
	{
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			AppUser sUser = (AppUser) attributes.getRequest().getSession().getAttribute(SESSION_USER_KEY);
			return sUser;
		}catch (Exception e){
			return null;
		}
	}

	/**
	 * 将用户信息放入SESSION
	 * @return
	 */
	public static synchronized AppUser setAppUser(AppUser appUser)
	{
		online++;
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpSession session = attributes.getRequest().getSession();
		session.setAttribute(SESSION_USER_KEY,appUser);
		session.setMaxInactiveInterval(0);
		sessions.put(session.getId(), appUser); 
		return appUser;
	}


	/**
	 * 将SESSION用户清空
	 * @return
	 */
	public static synchronized AppUser removeAppUser()
	{
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		AppUser appUser = getAppUser();
		if (appUser != null) {
			if (online > 0) {
				online --;
			}
			HttpSession session = attributes.getRequest().getSession();
			session.removeAttribute(SESSION_USER_KEY);
			sessions.remove(session.getId());
		}
		return appUser;
	}

	/**
	 * 检测当前用户是否存在
	 * @return
	 */
	public static boolean isLogin(String sessionId)
	{
		Object  sUser = sessions.get(sessionId);
		if(sUser == null)
		{
			return false ;
		}
		else
		{
			return true ;
		}
	}

	public static boolean isLogin(){
		if(getAppUser() == null){
			return false ;
		}else{
			return true ;
		}
	}

	public static class AppUser {

		private String userID;
 
		private String userName;
		
		private String realName;

		private String deptId; // 机构编码

		private String depName;

		public String getUserID() {
			return userID;
		}

		public void setUserID(String userID) {
			this.userID = userID;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getDeptId() {
			return deptId;
		}

		public void setDeptId(String deptId) {
			this.deptId = deptId;
		}

		public String getDepName() {
			return depName;
		}

		public void setDepName(String depName) {
			this.depName = depName;
		}

		public String getRealName() {
			return realName;
		}

		public void setRealName(String realName) {
			this.realName = realName;
		}
	}
}