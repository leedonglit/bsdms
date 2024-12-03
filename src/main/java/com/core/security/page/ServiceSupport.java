package com.core.security.page;

public abstract class ServiceSupport<T> {
	
	public abstract IDao<T,String> getDao();
	
}
