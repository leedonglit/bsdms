package com.core.security.database.jdbc.support.dialect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.core.security.bean.BeanReflactUtil;
import com.core.security.database.jdbc.annotation.DtoUtils;
import com.core.security.page.AutoPage;
import com.core.security.page.ColDefine;
import com.core.security.page.Page;
/**
 * Oracle数据库SQL语句生成类
 * @author: LiuTao
 * @since:Oct 16, 2012
 */
public class Oracledialect extends AbstractDialect {


	public String getPageSQL( Page<?> page , String findSql, String whereContent,String orderby) 
	{   
		if (whereContent == null)
		{
			whereContent = "";
		}
		int pageSize = page.getPageSize();
		int pageNo = page.getPageNo();
		String sql = "SELECT * FROM (SELECT T.*, ROWNUM NUM FROM (" + 
		findSql + ") T) WHERE NUM <= " + pageSize * pageNo + " AND NUM >=" + (pageSize * (pageNo - 1) + 1);
		return sql;
	}

	public String getPageCountSQL(String tableName, String whereContent) 
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
		Map<String, Object> map = BeanReflactUtil.bean2Map(obj);
		StringBuffer setsql = new StringBuffer();
		Object[] keys = map.keySet().toArray();
		for(Object key :keys){
			setsql.append(key).append("=?,");
		}
		setsql = setsql.delete(setsql.length()-1, setsql.length());
		String sql = "update "+dto.tableName()+" set "+setsql.toString()+" where "+dto.pkey()+" = ?";
		return sql ;
	}

//	public String getUpdateSQLForBatch(Object obj){
//	if(obj == null)
//	return null;
//	List<List<Object>> list = BeanReflectUtil.getFieldMethodList(obj);
//	DtoUtils dto = DtoUtils.getDtoInfo(obj.getClass());
//	StringBuffer _sql = new StringBuffer(" update "+dto.tableName()+" t set t.");
//	StringBuffer _field = new StringBuffer();
//	StringBuffer _content = new StringBuffer(" where t.");
//	for(List<Object> loc_list : list){
//	if(loc_list.get(3) != null){
//	if(!dto.pkey().equalsIgnoreCase(loc_list.get(0).toString().trim())){
//	_field.append(((String)loc_list.get(0)).trim()).append(" = ? , t.");
//	}else{
//	_content.append(((String)loc_list.get(0)).trim()).append(" = ?");	 
//	}
//	}
//	}
//	_field.delete(_field.length()-2, _field.length());
//	_sql.append(_field).append(_content);
//	ToolsLog.info(_sql.toString());
//	return _sql.toString();
//	}

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
	public static void main(String[] args){

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

//		"select * from ( select * from (select "+tableName+".*,rownum num from "+tableName+" "+whereContent+" ) where num <= "+pageSize * pageNo +") where num >="+(pageSize * (pageNo - 1)+1);

		int pageSize = page.getPageSize();
		int pageNo = page.getPageNo();
		sb.append("SELECT * FROM ( select t.*,ROWNUM AS RNUM FROM (SELECT ").append(fromContent+"."+selectContent)
		.append(" FROM ").append(fromContent).append(" WHERE ").append(whereContent);

		if (orderByContent == null || orderByContent.length() == 0||orderByContent.equals("")) {
			orderByContent = "";
		}else{
			orderByContent = " order by "+orderByContent;
		}
		sb.append(orderByContent).append(") T ) R WHERE RNUM BETWEEN ").append((pageSize * (pageNo - 1)+1)).append(" AND ").append(pageSize * pageNo);

//		sb.setLength(0);
//		sb.append("SELECT  ").append(" * ").append(" FROM (").append(" SELECT  ROW_NUMBER() OVER (ORDER BY ").append(orderByContent).append(") AS _xdfJdbcOrmRowNumber, ").append(selectContent).append(" FROM ").append(fromContent).append(" WHERE ").append(whereContent);
//		if (groupByContent != null && groupByContent.length() > 0) {
//		sb.append(" GROUP BY ").append(groupByContent);
//		}
//		sb.append(" ) AS _xdfJdbcLimitUnit WHERE _xdfJdbcLimitUnit._xdfJdbcOrmRowNumber > ").append(page.getPageStart());
//		sb.append(" and _xdfJdbcLimitUnit._xdfJdbcOrmRowNumber <= ").append(page.getPageStart()+page.getPageSize());
//		sql = sb.toString();
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

	@Override
	public String getPageSQL(Page<?> page, String sql) {
		return null;
	}
}
