package com.core.tools;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.core.security.database.jdbc.annotation.DTO;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;


/**
 * 数据库操作工具类
 * 常用于生成实体类
 * @author ldonglit
 * @since 2018-11-21
 */
public class DBTool {


	/**
	 * 根据默认数据源生成实体（datasource）
	 * @param tableName
	 */
	public static void getDTO(String tableName) {
		try {
			Connection ds = initDb();
			getTable(ds,tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String[] getTablePK(Connection conn, String tableCodes) throws Exception {
		String[] resultArray = null;
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getPrimaryKeys(conn.getCatalog(), conn.getSchema(), tableCodes);
		String tempPK = "";
		if (rs != null)
			while (rs.next()) {
				tempPK = rs.getString("COLUMN_NAME") + ",";
			}
		resultArray = tempPK.split(",");
		if (tempPK.length() < 1) {
			resultArray = null;
		}
		rs.close();
		return resultArray;
	}


	/**
	 * 获取数据库操作对象
	 * jdbc
	 * 连接池
	 * @return
	 */
	public static JdbcTemplate getJdbcTemplate(String username,String password,String url,String DBType) {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driverType(DBType));
		dataSource.setMaxActive(1);
		dataSource.setConnectTimeout(60*1000*60);
		return new JdbcTemplate(dataSource);
	}

	private static Connection initDb(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://119.189.0.152:3306/academic?useUnicode=true&useSSL=true&characterEncoding=UTF-8&serverTimezone=GMT%2b8", "root","ldonglit1q!");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static boolean isConnect(String username,String password,String url) throws SQLException{
		DruidDataSource dataSource = new DruidDataSource();
		DruidPooledConnection connection = null;
		try {
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dataSource.setMaxWait(6000);
			connection = dataSource.getConnection();
			return true;
		} catch (Exception e) {
			return false;
		}finally {
			connection.close();
			dataSource.close();
		}
	}

//	public static void main(String[] args) throws SQLException {
//		System.out.println(isConnect("root", "ldonglit1q!", "jdbc:mysql://3.91.68.39:3306/idgar?useSSL=false&characterEncoding=utf-8&autoReconnect=true"));
//		System.out.println(isConnect("root", "ldonglit1q!", "jdbc:mysql://3.91.68.139:3306/idgar?useSSL=false&characterEncoding=utf-8&autoReconnect=true"));
//	}

	private static void closeConnect(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}

	public static void getTable(Connection ds,String tableName) throws SQLException{
		DatabaseMetaData dbmd = ds.getMetaData();
		ResultSet rs = dbmd.getColumns(ds.getCatalog(), ds.getSchema(), tableName, "%");
		ResultSet rs1 = dbmd.getColumns(ds.getCatalog(), ds.getSchema(), tableName, "%");
		String str = "@" + DTO.class.getName() + "(";
		str = str + "tableName= \"" + tableName + "\"";
		String pks[] = null;
		try {
			pks = getTablePK(ds, tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<String> pkList = new ArrayList<String>();
		if (pks != null)
			for (int i = 0; i < pks.length; i++) {
				pkList.add(pks[i]);
			}
		if (pks != null && pks.length == 1)
			str = str + ", pkey= \"" + pks[0] + "\"";//此处有一个逻辑bug 没有对2个以上主键进行支持
		str = str + ")\n";
		str=str+"public class "+ getClassName(tableName) +"Dto implements java.io.Serializable{\n";
		str=str+"\n\tprivate static final long serialVersionUID = 1L;\n\n";
		while (rs.next()) {
			String type = rs.getString("TYPE_NAME");
			String name = rs.getString("COLUMN_NAME");
			String remark = rs.getString("REMARKS");
			if(null != pks && pks[0].equals(name)){
				str = str + "\t@javax.persistence.Id\n";
				//设置主键为自增长
				str = str + "\t@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)";
			}
			str = str + "\n\t@javax.persistence.Column(name=\""+name+"\")\n";
			str = str + "\tprivate " + beanType(type) + " " + name + ";"+"//"+remark;
			str = str + "\n";
		}

		while (rs1.next()) {
			String type = rs1.getString("TYPE_NAME");
			String name = rs1.getString("COLUMN_NAME");
			String name1 = name.substring(0,1).toUpperCase()+name.substring(1);
			str = str + "\n\tpublic " + beanType(type) + " get" + name1 + "(){\n\t\treturn  " + name + ";\n\t}";
			str = str + "\n\tpublic void  set" + name1 + "(" + beanType(type) + " " + name + "){\n\t\tthis." + name + "=" + name + ";\n\t}";
		}
		str = str +"\n}";
		System.out.println(str);
		closeConnect(ds);
	}

	public static String getClassName(String tn){
		if (tn.indexOf("_")>-1) {
			String [] sps = tn.split("_");
			if (sps.length>2) {
				return firstUpcase(sps[1])+firstUpcase(sps[2]);
			}else {
				return firstUpcase(sps[1]);
			}
		}else {
			return "null";
		}
	}

	public static String firstUpcase(String str) {
		str = str.toLowerCase();
		str = str.substring(0,1).toUpperCase()+str.substring(1,str.length());
		return str;
	}

	//首字母小写
	public static String toM(String tableName){
		StringBuilder sb = new StringBuilder();

		boolean low = true;
		for(int i=0; i<tableName.length(); i++){
			if(tableName.charAt(i)=='_'){
				low = false;
			}else{
				if(low){
					sb.append(tableName.charAt(i));
				}else{
					sb.append(Character.toUpperCase(tableName.charAt(i)));
					low=true;
				}
			}
		}
		return sb.toString();
	}

	//首字母大写
	public static String toL(String tableName){
		StringBuilder sb = new StringBuilder();

		boolean low = true;
		for(int i=0; i<tableName.length(); i++){
			if(tableName.charAt(i)=='_'){
				low = false;
			}else{
				if(low){
					sb.append(tableName.charAt(i));
				}else{
					sb.append(Character.toUpperCase(tableName.charAt(i)));
					low=true;
				}
			}
		}
		return sb.toString().substring(0,1).toUpperCase()+sb.toString().substring(1,sb.toString().length());
	}

	/*public static String capitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		// 针对aB模式进行处理
		if (name.length() > 1) {
			if (Character.isUpperCase(name.charAt(1)) && Character.isLowerCase(name.charAt(0))) {
				return name;
			}
		}
		return StringUtils.capitalize(name);
	}*/

	private static String beanType(String type) {
		switch (type) {
		case "VARCHAR":
		case "VARCHAR2":
		case "CHAR":
			return "String";
		case "INT":
		case "INTEGER":
		case "SMALLINT":
			return "Integer";
		case "BIGINT":
			return "Long";
		case "DATE":
		case "DATETIME":
		case "TIMESTAMP":
			return "Date";
		case "FLOAT":
			return "Float";
		case "DECIMAL":
		case "NUMERIC":
		case "DOUBLE":
			return "double";
		default:
			return "String";
		}
	}

	/**
	 * 通过数据库类型获取数据库驱动包地址
	 * @param dbType
	 * @return
	 */
	private static String driverType(String dbType) {
		switch (dbType) {
		case "mysql":
			return "com.mysql.jdbc.Driver";
		case "oracle":
			return "";
		}
		return "";
	}

	public static void runScript(String username,String password,String url,String path) throws SQLException{
		try {
			//注册驱动程序
			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
			//获得连接
			Connection con = DriverManager.getConnection(url, username, password);
			//初始化脚本运行器
			ScriptRunner sr = new ScriptRunner(con);
			//创建阅读器对象
			Reader reader = new BufferedReader(new FileReader(path));
			//运行脚本
			sr.runScript(reader);
			con.commit();
			con.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
