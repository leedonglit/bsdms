package com.core.security.database.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
/**
 * SpringJdbcTemplate协助类
 * @author: LiuTao
 * @since:Oct 16, 2012
 */
public interface  JdbcTemplateHelp {
	/**
	 * map类型时所需的结果集处理  
	 */
	public static RowMapper<Map<String,Object>> mapPRM = new ColumnMapRowMapper();
    
	
	public static RowMapper<Date> datePRM = new RowMapper<Date>() {
		public Date mapRow(ResultSet rs, int rownum) throws SQLException {
			return rs.getTime(1);
		}
	};
	public static RowMapper<Long> longPRM = new RowMapper<java.lang.Long>() {
		public Long mapRow(ResultSet rs, int rownum) throws SQLException {
			return rs.getLong(1);
		}
	};
	public static RowMapper<Double> doublePRM = new RowMapper<java.lang.Double>() {
		public Double mapRow(ResultSet rs, int rownum) throws SQLException {
			return rs.getDouble(1);
		}
	};
	public static RowMapper<Integer> intPRM = new RowMapper<java.lang.Integer>() {
		public Integer mapRow(ResultSet rs, int rownum) throws SQLException {
			return rs.getInt(1);
		}
	};
	public static RowMapper<Object> objPRM = new RowMapper<Object>() {
		public Object mapRow(ResultSet rs, int rownum) throws SQLException {
			return rs.getObject(1);
		}
	};
	public static RowMapper<String> stringPRM = new RowMapper<String>() {
		public String mapRow(ResultSet rs, int rownum) throws SQLException {
			return rs.getString(1);
		}
	};
	public static RowMapper<String> numberPRM = new RowMapper<String>() {
		public String mapRow(ResultSet rs, int rownum) throws SQLException {
			return rs.getString(1);
		}
	};

}
