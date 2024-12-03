package com.core.security.database.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;

import com.core.security.database.bind.DynamicDataSource;


@Configuration
public class JdbcDao{
	
	@Resource
	private DynamicDataSource dataSource;
	
	public DynamicDataSource getDataSource() {
		return dataSource;
	}
  
	public static void closeAll(Connection conn, Statement ps, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
