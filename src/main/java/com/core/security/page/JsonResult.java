package com.core.security.page;


import com.core.tools.PropertiesTool;
import lombok.Data;

@Data
public class JsonResult {

	public boolean success = true;
	public String  msg = "";
	public Object  data = null;

	public boolean sessiontimeout = false;

	public JsonResult() {
	}
	
	public JsonResult(Object data) {
		this.data = data;
	}

	public JsonResult(String msg,boolean success) {
		this.msg = msg;
		this.success = success;
	}
	
	public JsonResult(Object data,String msg,boolean success) {
		this.data = data;
		this.msg = msg;
		this.success = success;
	}


}
