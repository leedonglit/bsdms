package com.core.security.page;

import java.util.List;

public class PageBean {

	private List<Condition> condition;
	
	private Integer pageNo;
	
	private Integer pageSize;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public List<Condition> getCondition() {
		return condition;
	}

	public void setCondition(List<Condition> condition) {
		this.condition = condition;
	}
	
}
