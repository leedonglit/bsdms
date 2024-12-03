package com.core.tools;

public class FinalVarStaticTool {

	//登录用户在session中key名称
	public static String SESSION_USER_KEY = "_user_content";

	//拦截器-请求日志记录用户请求时间
	public static final String LOGGER_SEND_TIME = "_req_time";
	//拦截器-请求日志获取用户请求输入参数
	public static final String LOGGER_ENTITY = "_req_entity";
	//拦截器-请求日志获取用户请求返回数据
	public static final String LOGGER_RETURN = "_return_data";
	//拦截器-请求日志标记自定义日志说明
	public static final String LOG_REMARK = "_log_remark";
	
	//验证码session中表示key
	public static final String VALIDATE_CODE = "_val_code";
	
	public static boolean ISUPLOAD_DATA = false;
}
