package com.core.security.database.jdbc.support.dialect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;

import com.core.security.bean.BeanReflactUtil;
import com.core.security.database.jdbc.annotation.DtoUtils;
import com.core.security.page.AutoPage;
import com.core.security.page.ColDefine;
import com.core.security.page.Page;
/**
 * MySql 数据库 SQL语句资料库
 * @author 刘涛
 * @since Jul 2, 2012
 */
public class Mysqldialect extends AbstractDialect {

	public String getPageSQL( Page<?> page , String sql) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM  (");
		sb.append(sql);
		sb.append(") _P LIMIT ");
		sb.append((page.getPageNo()-1)*page.getPageSize());
		sb.append(",");
		sb.append(page.getPageSize());
		return sb.toString();
	}
	
	/**
	 * MYSQL分页语句	
	 */
	public String getPageSQL( Page<?> page , String tableName, String whereContent,String orderBy) {
		if(StringUtils.isBlank(whereContent))
		{
			whereContent=" 1 = 1";
		}
		if(StringUtils.isNotBlank(orderBy))
		{
			whereContent = whereContent +" order by "+ orderBy ;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM  ");
		sb.append(tableName);
		sb.append(" where ");
		sb.append(whereContent);
		sb.append(" LIMIT ");
		sb.append((page.getPageNo()-1)*page.getPageSize());
		sb.append(",");
		sb.append(page.getPageSize());
		
		return sb.toString();
	}
	/**
	 * MySql 分页条数统计语句
	 */
	public String getPageCountSQL (String tableName ,String whereContent)
	{


		if(whereContent == null || "".equals(whereContent) )
		{
			whereContent = "" ;
		}else {
			whereContent = " where " +whereContent;
		}
		String sql = "select count(*) from "+tableName+" "+whereContent;
		return sql;
	}

	public String getDeleteSQL(String tableName,String pkName) 
	{
		String sql = "delete from "+tableName+" where "+pkName+" = ? ";
		return sql;
	}

	public String getInsertSQL(String tableName,Object obj) 
	{
		//Map<String, Object> map = BeanReflectUtil.beanAnnotation2Map(obj);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj;
		StringBuffer sb = new StringBuffer();
		StringBuffer sbparam = new StringBuffer();
		Object[] keys = map.keySet().toArray();
		for(Object key :keys)
		{
			sb.append(key).append(",");
			sbparam.append("? ,");
		}
		sb = sb.delete(sb.length()-1, sb.length());
		sbparam = sbparam.delete(sbparam.length()-1, sbparam.length());
		String sql = "insert into "+tableName+" ( "+sb.toString()+" ) values ( "+sbparam.toString()+" )";
		return sql;
	}
	public String getInsertSQL(String tableName,Map<String, Object> map) 
	{
		//		Map<String, Object> map = BeanReflectUtil.beanAnnotation2Map(obj);
		StringBuffer sb = new StringBuffer();
		StringBuffer sbparam = new StringBuffer();
		Object[] keys = map.keySet().toArray();
		for(Object key :keys)
		{
			sb.append(key).append(",");
			sbparam.append("? ,");
		}
		sb = sb.delete(sb.length()-1, sb.length());
		sbparam = sbparam.delete(sbparam.length()-1, sbparam.length());
		String sql = "insert into "+tableName+" ( "+sb.toString()+" ) values ( "+sbparam.toString()+" )";
		return sql;
	}
	/**
	 * 根据输入List field的顺序 生成sql语句
	 */
	public String getOrderedInsertSQL(String tableName,List<List<Object>> field){
		StringBuffer _tempf = new StringBuffer();
		StringBuffer _templ = new StringBuffer();
		StringBuffer _sql = new StringBuffer();
		for(List<Object> _list : field){
			_tempf.append(_list.get(0)).append(",");
			_templ.append("?,");
		}
		_tempf.delete(_tempf.length()-1, _tempf.length());
		_templ.delete(_templ.length()-1, _templ.length());
		_sql.append("insert into ").append(tableName).append(" ( ").append(_tempf).append(" ) values ( ");
		_sql.append(_templ).append(" ) ");
		return _sql.toString();
	}

	public String getUpdateSQL(Object obj) 
	{
		Class<?> clazz = obj.getClass();
		DtoUtils dto = DtoUtils.getDtoInfo(clazz);
		Map<String, Object> map = BeanReflactUtil.beanAnnotation2Map(obj);
		StringBuffer setsql = new StringBuffer();
		Object[] keys = map.keySet().toArray();
		for(Object key :keys){
			setsql.append(key).append("=?,");
		}
		setsql = setsql.delete(setsql.length()-1, setsql.length());
		String sql = "update "+dto.tableName()+" set "+setsql.toString()+" where "+dto.pkey()+" = ?";
		return sql ;
	}

	public String getUpdateSQLForBatch(Object obj,Queue<List<Object>> queue){
		if(obj == null)
			return null;
		DtoUtils dto = DtoUtils.getDtoInfo(obj.getClass());
		StringBuffer _sql = new StringBuffer(" update "+dto.tableName()+" t set t.");
		StringBuffer _field = new StringBuffer();
		StringBuffer _content = new StringBuffer(" where t.");
		while(queue.size()>1){
			List<Object> list = queue.poll();
			_field.append(((String)list.get(0)).trim()).append(" = ? , t.");
		}
		List<Object> list_content = queue.poll();
		_content.append(((String)list_content.get(0)).trim()).append(" = ?");	 
		_field.delete(_field.length()-4, _field.length());
		_sql.append(_field).append(_content);
		return _sql.toString();
	}
	public String getSelectSQL(String tableName, String pkName) {
		String sql = "select * from "+tableName+"  where  "+pkName+" = ?";
		return sql;
	}

	public String getDeleteSQL(String tableName) {
		String sql = "delete from "+ tableName + "";
		return sql;
	}

	public String getSelectSQL(String tableName) {
		String sql = "select * from "+tableName+"" ;
		return sql;
	}

	public String getPageSQL(Page<?> page, String selectContent,
			String fromContent, String whereContent, String orderByContent,
			String groupByContent, Object... params) {
		page = (AutoPage<?>)page;
		StringBuilder sb = new StringBuilder();
		if (whereContent == null || whereContent.isEmpty())
			whereContent = " 1=1 ";
		if (whereContent.trim().startsWith("and"))
			whereContent = whereContent.trim().substring(3);
		if (whereContent.trim().startsWith("AND"))
			whereContent = whereContent.trim().substring(3);
		int pageSize = page.getPageSize();
		int pageNo = page.getPageNo();
		//TODO 此处拼接SQL去除 fromContent+"."+的字符串拼接.用于实现联合查询的SQL适应.不会影响其他的内容 
		//        sb.append("SELECT * FROM ( select t.*,rownum  as  num from (select ").append(fromContent+"."+selectContent)
		sb.append("select t.* from (select ").append(selectContent)
		.append(" from ").append(fromContent).append(" WHERE ").append(whereContent);
		if (orderByContent == null || orderByContent.length() == 0||orderByContent.equals("")) {
			orderByContent = "";
		}else{
			orderByContent = " order by "+orderByContent;
		}
		//sb.append(orderByContent).append(") t LIMIT 0,").append(pageSize * pageNo+") ss limit ").append((pageSize * (pageNo - 1)+1));
		sb.append(orderByContent).append(") t LIMIT "+pageSize * (pageNo - 1)+",").append(pageSize);
		String sql = sb.toString();
		return sql;
	}

	/**
	 * 自定义联合查询总数SQL
	 * @return: String
	 * @Author: WangHuidong <BR>
	 * @Datetime：2014-7-22 上午9:28:55 <BR>
	 */
	public String getCustomPageCountSQL(String fromContent, String whereContent) {
		StringBuilder sb = new StringBuilder();
		if (whereContent == null || whereContent.isEmpty())
			whereContent = " 1=1 ";
		if (whereContent.trim().startsWith("and"))
			whereContent = whereContent.trim().substring(3);
		if (whereContent.trim().startsWith("AND"))
			whereContent = whereContent.trim().substring(3);

		sb.append("SELECT count(1) FROM ").append(fromContent).append(" WHERE ").append(whereContent);
		String sql = sb.toString();
		return sql;
	}
	public String getConditionWhere(ColDefine[] condition) {
		ArrayList<ColDefine> al = new ArrayList<ColDefine>();
		for (int i = 0; i < condition.length; i++){
			if ("".equals(condition[i]+"") || null == condition[i]) {
				continue;
			}
			al.add(condition[i]);
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0,j=al.size(); i < j; i++) {
			if(i!=j-1){
				if("like".equals(al.get(i).getType())){
					sb.append(al.get(i).getCol() +" "+ al.get(i).getType() +" '%"+al.get(i).getValue()[0].toString()+"%' and ");
				}else{
					if(al.get(i).getCol().indexOf("TIME")>-1||al.get(i).getCol().indexOf("DATE")>-1){
						sb.append(al.get(i).getCol() +" "+ al.get(i).getType() +" to_date('"+al.get(i).getValue()[0].toString()+"','yyyy-MM-dd') and ");
					}else{
						sb.append(al.get(i).getCol() +" "+ al.get(i).getType() +" '"+al.get(i).getValue()[0].toString()+"' and ");
					}
				}
			}else{
				if("like".equals(al.get(i).getType())){
					sb.append(al.get(i).getCol() +" "+ al.get(i).getType() +" '%"+al.get(i).getValue()[0].toString()+"%'");
				}else{
					if(al.get(i).getCol().indexOf("TIME")>-1||al.get(i).getCol().indexOf("DATE")>-1){
						sb.append(al.get(i).getCol() +" "+ al.get(i).getType() +" to_date('"+al.get(i).getValue()[0].toString()+"','yyyy-MM-dd')");
					}else{
						sb.append(al.get(i).getCol() +" "+ al.get(i).getType() +" '"+al.get(i).getValue()[0].toString()+"'");
					}
				}
			}
		}
		return sb.toString();
	}




}
