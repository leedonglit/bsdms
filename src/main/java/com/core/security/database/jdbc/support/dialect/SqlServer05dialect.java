package com.core.security.database.jdbc.support.dialect;

import java.util.List;
import java.util.Queue;

import com.core.security.page.ColDefine;
import com.core.security.page.Page;

public class SqlServer05dialect extends AbstractDialect {

	public String getPageSQL(Page<?> page ,  String tableName, String whereContent ,String orderby) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPageCountSQL(String tableName, String whereContent) {
		return null;
	}

	public String getDeleteSQL(String tableName,String pkName) {
		// TODO Auto-generated method stub
		return null;
	}




	public String getInsertSQL(String tableName, Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUpdateSQL(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSelectSQL(String tableName, String pkName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDeleteSQL(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSelectSQL(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPageSQL(Page<?> page, String selectContent,
			String fromContent, String whereContent, String orderByContent,
			String groupByContent, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOrderedInsertSQL(String tableName, List<List<Object>> field) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUpdateSQLForBatch(Object obj, Queue<List<Object>> queue) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCustomPageCountSQL(String fromContent, String whereContent) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getConditionWhere(ColDefine[] condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPageSQL(Page<?> page, String sql) {
		// TODO Auto-generated method stub
		return null;
	}

}
