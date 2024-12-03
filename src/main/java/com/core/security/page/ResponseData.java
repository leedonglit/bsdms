package com.core.security.page;

public class ResponseData {
	private String msg;
	private Integer code;
	public ResponseData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ResponseData(String msg, Integer code) {
		super();
		this.msg = msg;
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
}
