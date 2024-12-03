package com.core.security.database.jdbc.support.dialect;

public class SqlDialectFactory{

	public static AbstractDialect createSQLdialect(String dbType){
		AbstractDialect sQLdialect = null;
		if(dbType.equals("mysql"))
		{
			sQLdialect = new Mysqldialect();
		}
		else if(dbType.equals("oracle"))
		{
			sQLdialect = new Oracledialect();
		}
		else if(dbType.equals("sqlserver2005"))
		{
			sQLdialect = new SqlServer05dialect();
		}
		else if(dbType.equals("sqlserver2008"))
		{
			sQLdialect = new SqlServer05dialect();
		}else if(dbType.equals("kingbase")){
			sQLdialect = new Oracledialect();
		} 
		return sQLdialect;
	}

}
