package com.core.extend.websocket;

import javax.websocket.Session;

public class WsSession {
	private Session session; 
	private String className;
	private String userid;
	
	public WsSession(Session session, String className, String userid) {
		super();
		this.session = session;
		this.className = className;
		this.userid = userid;
	}
	
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
}
