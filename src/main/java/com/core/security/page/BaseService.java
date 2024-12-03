package com.core.security.page;

public abstract class BaseService<T> extends ServiceSupport<T>{

	public AutoPage<T> findAutoPage(AutoPage<T> page) {
		return this.getDao().findAutoPage(page);
	}
}
