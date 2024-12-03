package com.core.config;

import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.core.tools.AppUserTool;

@WebListener
public class SessionConfig implements HttpSessionListener,HttpSessionAttributeListener{

	@Override
	public void attributeAdded(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		ServletContext context = session.getServletContext();
		@SuppressWarnings("unchecked")
		HashSet<Object> sessions = (HashSet<Object>) context.getAttribute("sessions");
		if (sessions == null) {
			sessions = new HashSet<Object>();
			context.setAttribute("sessions", sessions);
		}
		sessions.add(session);
		AppUserTool.online = sessions.size();
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		ServletContext context = session.getServletContext();
		@SuppressWarnings("unchecked")
		HashSet<Object> sessions = (HashSet<Object>) context.getAttribute("sessions");
		sessions.remove(session);
		AppUserTool.online = sessions.size();
	}

}
