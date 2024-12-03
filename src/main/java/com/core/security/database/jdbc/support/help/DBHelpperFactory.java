package com.core.security.database.jdbc.support.help;

public class DBHelpperFactory {

	public static AbstractDBHelpper createDBHelpper(String dbType){
		AbstractDBHelpper helpper = null;
		if("mysql".equals(dbType))
		{
			helpper = new MysqlHelpper();
		}
		else if("oracle".equals(dbType))
		{
			helpper = new OracleHelpper();
		}
		else if("sqlserver2005".equals(dbType))
		{
			helpper = new SqlServer05Helpper();
		}
		else if("sqlserver2008".equals(dbType))
		{
			helpper = new SqlServer08Helpper();
		}else if("kingbase".equals(dbType)){
			helpper = new KingBaseHelper();
		} 
		return helpper;
	}

}
