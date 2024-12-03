package com.core.security.database.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
	private static Connection connection=null;
	public static Connection conn(String diver,String url,String userName,String password) {
		Connection connection=null;
		try {
			Class.forName(diver);
			try {
				connection = DriverManager.getConnection(url, userName, password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
	}
	public static void close() {
			try {
				if(connection!=null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
}
