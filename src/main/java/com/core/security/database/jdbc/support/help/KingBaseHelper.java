package com.core.security.database.jdbc.support.help;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.util.Date;

public class KingBaseHelper implements AbstractDBHelpper{
	
	public Method getPrepareStatementMethod(PreparedStatement ps,
			Class<?> FieldType) throws SecurityException, NoSuchMethodException {
		if(String.class.equals(FieldType)){
			return ps.getClass().getSuperclass().getDeclaredMethod("setString",int.class,java.lang.String.class );
		}else if(BigDecimal.class.equals(FieldType)){
			return ps.getClass().getSuperclass().getDeclaredMethod("setBigDecimal",int.class,java.math.BigDecimal.class );
		}else if(Date.class.equals(FieldType)){
			return ps.getClass().getSuperclass().getDeclaredMethod("setTimestamp",int.class,java.sql.Timestamp.class );
		}else if(Blob.class.equals(FieldType)){
			return ps.getClass().getSuperclass().getDeclaredMethod("setBlob", int.class,java.sql.Blob.class);
		}else if(Clob.class.equals(FieldType)){
			return ps.getClass().getSuperclass().getDeclaredMethod("setClob", int.class,java.sql.Clob.class);
		}else if(Integer.class.equals(FieldType)){
			return ps.getClass().getSuperclass().getDeclaredMethod("setInt",int.class,int.class );
		}
		return null;
	}

}
