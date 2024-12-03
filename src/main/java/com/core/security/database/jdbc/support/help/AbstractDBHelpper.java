package com.core.security.database.jdbc.support.help;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;

/**
 * 数据库的帮助接口
 * @author 韩泉
 *
 */
public interface AbstractDBHelpper {

	/**
	 * 根据Field的类型得到预处理对象相应的setter方法对象
	 * @param ps 预处理对象
	 * @param FieldType 字段的类型
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public Method getPrepareStatementMethod(PreparedStatement ps,Class<?> FieldType) throws SecurityException, NoSuchMethodException;
}
