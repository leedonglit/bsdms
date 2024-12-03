package com.core.security.database.jdbc.support.dialect;


import java.util.List;
import java.util.Queue;

import com.core.security.page.ColDefine;
import com.core.security.page.Page;
/**
 * 数据库SQL语句统一调度生成类
 * @author 刘涛
 * @since Jul 2, 2012
 */
public interface SQLdialect {
	
	
	public String getPageSQL( Page<?> page ,  String sql);
	/**
	 * 获取分页查询语句
	 * @param page
	 * @param tableName
	 * @param whereContent
	 * @return
	 */
	public String getPageSQL( Page<?> page ,  String tableName , String whereContent , String orderBy);
	
	/**
	 * 
	 * @param page				
	 * @param selectContent
	 * @param fromContent
	 * @param whereContent
	 * @param orderByContent
	 * @param groupByContent
	 * @param params
	 * @return
	 */
	public String getPageSQL(Page<?> page,String selectContent,
			String fromContent, String whereContent, String orderByContent,
			String groupByContent, Object... params);
	
	/**
	 * 自定义联合查询总数SQL
	 * @return: String
	 * @Author: WangHuidong <BR>
	 * @Datetime：2014-7-22 上午9:33:24 <BR>
	 */
	public String getCustomPageCountSQL(String fromContent, String whereContent);
	
	/**
	 * 获取统计查询语句
	 * @param tableName
	 * @param whereContent
	 * @return
	 */
	public String getPageCountSQL (String tableName ,String whereContent);
	
	/**
	 * 获得数据库有序插入语句
	 * @param tableName
	 * @param field
	 * @return
	 */
	public String getOrderedInsertSQL(String tableName,List<List<Object>> field);
	/**
	 * 获取数据库插入语句
	 * @return
	 */
	public String getInsertSQL(String tableName,Object obj);
	/**
	 * 获取数据库更新语句
	 * @return
	 */
	public String getUpdateSQL(Object obj);
	/**
	 * 获取用于数据库批量更新语句
	 * @param obj   数据库实体对象
	 * @param queue 实体有效字段队列，其中列尾是主键值，以主键为更新条件
	 * @return
	 */
	public String getUpdateSQLForBatch(Object obj,Queue<List<Object>> queue);
	
	
	/**
	 * 获取数据库删除语句
	 * @return
	 */
	public String getDeleteSQL(String tableName,String pkName);
	/**
	 * 获取数据库删除语句
	 * @return
	 */
	public String getDeleteSQL(String tableName);
	/**
	 * 获取查询语句
	 * @param tableName
	 * @param pkName
	 * @return
	 */
	public String getSelectSQL(String tableName,String pkName);
	/**
	 * 获取查询语句
	 * @param tableName
	 * @return
	 */
	public String getSelectSQL(String tableName);
	
	
	/**
	 * 获取查询条件中的参数及值组合where条件
	 * @param condition
	 * @return
	 */
	public String  getConditionWhere(ColDefine[] condition) ;
}
