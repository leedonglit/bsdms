package com.core.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyExceptionHandler {


	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Map<String,Object> handleException(Exception e){
		e.printStackTrace();
		Map<String,Object> map=new HashMap<>(16);
		map.put("code","-101");
		map.put("msg",e.getMessage());
		return map;

	}
}
