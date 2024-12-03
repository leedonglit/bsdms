package com.core.security.database.jdbc;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.core.security.bean.BeanReflactUtil;
import com.core.security.database.bind.DynamicDataSource;
import com.core.security.database.bind.DynamicDataSourceContextHolder;
import com.core.security.database.jdbc.annotation.DTO;
import com.core.security.database.jdbc.annotation.DtoUtils;
import com.core.security.database.jdbc.support.JdbcTemplateHelp;
import com.core.security.database.jdbc.support.dialect.AbstractDialect;
import com.core.security.database.jdbc.support.dialect.SqlDialectFactory;
import com.core.security.database.jdbc.support.help.AbstractDBHelpper;
import com.core.security.database.jdbc.support.help.DBHelpperFactory;
import com.core.security.page.AutoPage;
import com.core.security.page.ColDefine;
import com.core.security.page.IDao;
import com.core.security.page.PageHelp;

public class BaseDao<T, PK extends Serializable> extends JdbcDao implements IDao<T, PK>{

	protected Class<T> entityClass = null;
	String pkey = "";
	String pkeyFiled = "";//主键对应的字段名称
	String tableName;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public BaseDao() {
		try {
			entityClass = BeanReflactUtil.getSuperClassGenricType(getClass());
			DtoUtils dto = DtoUtils.getDtoInfo(entityClass);
			if (dto != null) {
				tableName = dto.tableName();
				pkey = dto.pkey();
				pkeyFiled=dto.pkeyFiled();
			} else {
				tableName = getTableName(entityClass);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private String getDBType(){
		DynamicDataSource druidDataSource = getDataSource();
		return DynamicDataSourceContextHolder.getDBType(druidDataSource.determineCurrentLookupKey());
	}

	private AbstractDialect getDialect() {

		return SqlDialectFactory.createSQLdialect(getDBType());
	}

	private AbstractDBHelpper getHelpper() {

		return DBHelpperFactory.createDBHelpper(getDBType());
	}

	public String getTableName() {
		return tableName;
	}

	public String getPkey() {
		return pkey;
	}

	private String getTableName(Class<T> entityClass) {
		return entityClass.getSimpleName();
	}

	public void excute(String sql, Object... objects) {
		this.getJdbcTemplate().update(sql, objects);
	}

	public void excute(String sql) {
		this.getJdbcTemplate().execute(sql);;
	}

	public void excute(String[] sqls) {
		this.getJdbcTemplate().batchUpdate(sqls);
	}

	public Integer getInt(String sql, Object... objects) {
		return this.getJdbcTemplate().queryForObject(sql,Integer.class, objects);
	}

	public List<T> getList(String sql, Object... objects) {
		return getList(sql, entityClass, objects);
	}

	// * 通过单个对象属性查找对象
	public List<T> findByProperty(String propertyName,Object value){
		String sql="select * from "+tableName+" where " + propertyName + "= ?";
		List<T> ifs = (List<T>) this.getJdbcTemplate().query(sql, getRowMapper(entityClass), value);
		return ifs;
	}

	// * 通过单个对象属性查找对象
	public List<T> findByPropertySort(String propertyName,Object value,String filed,String dir){
		if (StringUtils.isEmpty(dir)) {
			dir = "asc";
		}
		String sql="select * from "+tableName+" where " + propertyName + "= ? order by "+filed + " " + dir;
		List<T> ifs = (List<T>) this.getJdbcTemplate().query(sql, getRowMapper(entityClass), value);
		return ifs;
	}

	public List<T> getList(String sql, Class<T> clazz, Object... objects) {
		List<T> ifs = (List<T>) this.getJdbcTemplate().query(sql, getRowMapper(clazz), objects);
		return ifs;
	}

	public List<?> getListBySql(String sql, Class<?> cla, Object... objects) {
		if (cla.isAnnotationPresent(DTO.class))
			return getJdbcTemplate().query(sql, getRowMapper(cla), objects);
		else
			return null;

	}

	public List<Map<String, Object>> getMapList(String sql){

		return this.getJdbcTemplate().queryForList(sql);
	}

	public Map<String, Object> getMap(String sql){
		List<Map<String, Object>> list = getMapList(sql);
		if (null != list && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	// 待修改
	public T getObject(String sql, Object... objects) {
		T obj = null;
		try {
			obj = this.getJdbcTemplate().queryForObject(sql, getRowMapper(entityClass), objects);
		} 
		catch (Exception e) {
			if (e instanceof IncorrectResultSizeDataAccessException)
				return null;
		}
		return obj;

	}

	// 待修改
	public T getObject(PK key) {
		String sql = getDialect().getSelectSQL(tableName, this.pkey);
		T obj = null;
		try {
			obj = this.getJdbcTemplate().queryForObject(sql, getRowMapper(entityClass), new Object[] { key });
		} catch (Exception e) {
			if (e instanceof IncorrectResultSizeDataAccessException)
				return null;
		}
		return obj;
	}
	// 待修改
	public T getObject(Integer key) {
		String sql = getDialect().getSelectSQL(tableName, this.pkey);
		T obj = null;
		try {
			obj = this.getJdbcTemplate().queryForObject(sql, getRowMapper(entityClass), new Object[] { key });
		} catch (Exception e) {
			if (e instanceof IncorrectResultSizeDataAccessException)
				return null;
		}
		return obj;
	}
	// 待修改
	public T getObject(long key) {
		String sql = getDialect().getSelectSQL(tableName, this.pkey);
		T obj = null;
		try {
			obj = this.getJdbcTemplate().queryForObject(sql, getRowMapper(entityClass), new Object[] { key });
		} catch (Exception e) {
			if (e instanceof IncorrectResultSizeDataAccessException)
				return null;

		}
		return obj;
	}
	public Object getObjectBySql(String sql, Class<?> cla, Object... objects) {
		Object obj = null;
		if (cla.isAnnotationPresent(DTO.class)) {
			List<?> ifs = this.getJdbcTemplate().query(sql, getRowMapper(cla), objects);
			if (ifs.size() >= 0)
				obj = ifs.get(0);
		}
		return obj;
	}

	public void saveAll(List<T> list) {
		if (null != list && list.size() > 0) {
			for (T obj : list)
				this.saveObject(obj);
		}
	}

	public String saveObject(T obj) {
		if (obj == null)
			return null;
		if (obj.getClass().isAnnotationPresent(DTO.class)) {
			Class<?> clazz = obj.getClass();
			DtoUtils dto = DtoUtils.getDtoInfo(clazz);
			if (dto != null) {
				return insert(dto.tableName(), obj);
			} else
				return null;
		} else {
			return null;
		}
	}

	public T saveObjectByAutoUUID(T obj) {
		if (obj == null)
			return null;
		if (obj.getClass().isAnnotationPresent(DTO.class)) {
			Class<?> clazz = obj.getClass();
			DtoUtils dto = DtoUtils.getDtoInfo(clazz);
			if (dto != null) {
				String uuid = java.util.UUID.randomUUID().toString();
				BeanReflactUtil.setFieldValue(obj, dto.pkey(), uuid);
				insert(dto.tableName(), obj);
				return obj;
			} else
				return null;
		} else {
			return null;
		}
	}

	private String insert(String tableName, Object obj) {
		Map<String, Object> map = BeanReflactUtil.beanAnnotation2Map(obj);
		final Object[] prObj=map.values().toArray();
		final String sql = getDialect().getInsertSQL(tableName, map);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection)
					throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] {pkey});
				for(int i=0;i<prObj.length;i++){
					if(prObj[i]==null){
						ps.setNull(i + 1, java.sql.Types.NULL);
					}else if (prObj[i] instanceof Integer) {
						int value = ((Integer) prObj[i]).intValue();
						ps.setInt(i + 1, value);
					} else if (prObj[i] instanceof String) {
						String s = (String) prObj[i];
						ps.setString(i + 1, s);
					} else if (prObj[i] instanceof Double) {
						double d = ((Double) prObj[i]).doubleValue();
						ps.setDouble(i + 1, d);
					} else if (prObj[i] instanceof Float) {
						float f = ((Float) prObj[i]).floatValue();
						ps.setFloat(i + 1, f);
					} else if (prObj[i] instanceof Long) {
						long l = ((Long) prObj[i]).longValue();
						ps.setLong(i + 1, l);
					} else if (prObj[i] instanceof Boolean) {
						boolean b = ((Boolean) prObj[i]).booleanValue();
						ps.setBoolean(i + 1, b);
					} else if (prObj[i] instanceof Date) {
						Timestamp d = new Timestamp(((java.util.Date) prObj[i]).getTime());
						ps.setTimestamp(i + 1, d);
					}
				}
				return ps;
			}

		}, keyHolder);
		return keyHolder.getKeys()==null?"":keyHolder.getKeys().get(pkey).toString();
	}

	public void updateAll(List<T> list) {
		if (null != list && list.size() > 0) {
			for (T obj : list)
				updateObject(obj);
		}
	}

	public T updateObject(T obj) {
		if (obj == null) {
			return null;
		}
		Class<?> clazz = obj.getClass();
		DtoUtils dto = DtoUtils.getDtoInfo(clazz);
		if (dto != null) {
			Map<String, Object> map = BeanReflactUtil.beanAnnotation2Map(obj);
			String sql = getDialect().getUpdateSQL(obj);
			Object[] param = new Object[map.values().size() + 1];
			System.arraycopy(map.values().toArray(), 0, param, 0, param.length - 1);
			param[map.values().size()] = map.get(dto.pkey());
			this.getJdbcTemplate().update(sql, param);
			return obj;
		} else {
			return null;
		}
	}

	public String getKeyName() {
		return pkey;
	}
	public String getKeyFiled() {
		return pkeyFiled;
	}

	public void deleteObject(PK pk) {
		String sql = getDialect().getDeleteSQL(this.tableName, this.pkey);
		this.getJdbcTemplate().update(sql, pk);
	}

	public AutoPage<T> findPage(AutoPage<T> page) {
		String whereContent = PageHelp.getWhere(page);
		String orderby = PageHelp.getOrderby(page);
		String countSQL = getDialect().getPageCountSQL(tableName, whereContent);
		int ICount = this.getJdbcTemplate().queryForObject(countSQL, Integer.class);
		page.setTotalCount(ICount);
		page.setTotalpage((ICount + page.getPageSize() -1) / page.getPageSize());
		String pageSQL = getDialect().getPageSQL(page, tableName, whereContent , orderby);
		List<T> pageList = this.getList(pageSQL, this.entityClass);
		page.setResult(pageList);
		return page;
	}
	/*
	public Page<T> findPage(Page<T> page, String whereContent) {
		String countSQL = getDialect().getPageCountSQL(tableName, whereContent);
		int ICount =  this.getJdbcTemplate().queryForObject(countSQL, Integer.class);
		page.setTotalCount(ICount);
		String pageSQL = getDialect().getPageSQL(page, tableName, whereContent);
		List<T> pageList = this.getList(pageSQL, this.entityClass);
		page.setResult(pageList);
		return page;
	}*/

	public void deleteObjectBySql(String sql, Object... objects) {
		this.getJdbcTemplate().update(sql, objects);
	}

	public int deleteAll() {
		String sql = getDialect().getDeleteSQL(tableName);
		int i = this.getJdbcTemplate().update(sql);
		return i;
	}

	public List<T> getAllList() {
		String sql = getDialect().getSelectSQL(tableName);
		List<T> entityList = this.getJdbcTemplate().query(sql, getRowMapper(entityClass));
		return entityList;
	}

	public int[] batchInsert(String tableName, final List<T> list) {
		String sql = getDialect().getOrderedInsertSQL(tableName, BeanReflactUtil.getFieldMethodList(list.get(0)));
		BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				T obj = list.get(i);
				List<List<Object>> list = BeanReflactUtil.getFieldMethodList(obj);
				try {
					for (int j = 0, k = list.size(); j < k; j++) {
						Method _ps = getHelpper().getPrepareStatementMethod(ps, (Class<?>) list.get(j).get(1));
						Method _obj = (Method) list.get(j).get(2);
						if (Date.class.equals((Class<?>) (list.get(j).get(1)))) {
							if(_obj.invoke(obj)!=null){
								_ps.invoke(ps, j + 1, new Timestamp(((Date)_obj.invoke(obj)).getTime()));
							}else{
								_ps.invoke(ps, j + 1, null);
							}
							//_ps.invoke(ps, j + 1, (Timestamp) _obj.invoke(obj));
						} else {
							_ps.invoke(ps, j + 1, _obj.invoke(obj));
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
			public int getBatchSize() {
				return list.size();
			}
		};
		return this.getJdbcTemplate().batchUpdate(sql, setter);
	}

	public int[] batchUpdate(final List<T> list) {
		String sql = getDialect().getOrderedInsertSQL(tableName, BeanReflactUtil.getFieldMethodList(list.get(0)));
		BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				T obj = list.get(i);
				Queue<List<Object>> queue = BeanReflactUtil.getValidateField(obj);
				try {
					int j = 1;
					while (queue.size() != 0) {
						List<Object> _list = queue.poll();
						Method _ps = getHelpper().getPrepareStatementMethod(ps, (Class<?>) _list.get(1));
						Method _obj = (Method) _list.get(2);
						if (Date.class.equals((Class<?>) (_list.get(1)))) {
							_ps.invoke(ps, j, (Timestamp) _obj.invoke(obj));
						} else {
							_ps.invoke(ps, j, _obj.invoke(obj));
						}
						j++;
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}

			}

			public int getBatchSize() {
				return list.size();
			}

		};
		return this.getJdbcTemplate().batchUpdate(sql, setter);

	}

	public String getConditionWhere(AutoPage<T> page) {
		ColDefine[] condition = page.getCondition();
		return getDialect().getConditionWhere(condition);
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	private <T> RowMapper<T> getRowMapper(Class<T> clazz) {
		if (String.class.equals(clazz))
			return (RowMapper<T>) JdbcTemplateHelp.stringPRM;
		else if (Long.class.equals(clazz))
			return (RowMapper<T>) JdbcTemplateHelp.longPRM;
		else if (Date.class.equals(clazz))
			return (RowMapper<T>) JdbcTemplateHelp.datePRM;
		else if (Object.class.equals(clazz))
			return (RowMapper<T>) JdbcTemplateHelp.objPRM;
		else if (Integer.class.equals(clazz))
			return (RowMapper<T>) JdbcTemplateHelp.intPRM;
		else if (Double.class.equals(clazz))
			return (RowMapper<T>) JdbcTemplateHelp.doublePRM;
		else if (Map.class.equals(clazz))
			return (RowMapper<T>) JdbcTemplateHelp.mapPRM;
		else if (Number.class.equals(clazz))
			return (RowMapper<T>) JdbcTemplateHelp.numberPRM;
		else
			return BeanPropertyRowMapper.newInstance(clazz);
	}

	public void deleteByProperty(String propertyName, Object value) {
		String sql="delete from "+tableName+" where " + propertyName + "= ?";
		this.excute(sql,value);
	}

	public AutoPage<T> findAutoPageBySQL(AutoPage<T> page, String sql) {
		String _sql = this.getDialect().getPageSQL(page, sql);
		int ICount = this.getJdbcTemplate().queryForObject("select count(1) from (" + sql + ") C", Integer.class);
		page.setTotalCount(ICount);
		page.setTotalpage((ICount + page.getPageSize() -1) / page.getPageSize());
		List<T> resultList = getList(_sql, new Object[0]);
		page.setResult(resultList);
		return page;
	}

	@Override
	public AutoPage<T> findAutoPage(AutoPage<T> page) {
		return findAutoPage(page, this.getTableName(), this.getKeyName());
	}

	public AutoPage<T> findAutoPage(AutoPage<T> page, String tableName, String keyName) {
		if (page == null)
			page = new AutoPage<T>();
		List<Object> paramsList = new ArrayList<Object>();
		String whereContent = PageHelp.getWhere(page);
		String orderby = PageHelp.getOrderby(page);
		page.setKey(pkeyFiled);
		// XXX 暂时不对分页对象中进行分组支持
		Map<String, Object> map = page.getParams();
		if (map != null && map.get("expend") != null) {
			// String expendCondion = (String)map.get("expend");
			// return findPage(page, " * ", tableName, whereContent+" "+
			// expendCondion, orderby,null, paramsList.toArray());
		} else {
			return findAutoPage(page, " * ", tableName, whereContent, orderby, null, paramsList.toArray());
		}
		return null;
	}

	public AutoPage<T> findAutoPage(AutoPage<T> page, String selectContent, String fromContent, String whereContent, String orderByContent,
			String groupByContent, Object... params) {
		String sql = "";
		String countSQL = "";
		if (!whereContent.equals(""))
			countSQL = getDialect().getPageCountSQL(tableName, whereContent);
		else
			countSQL = getDialect().getPageCountSQL(tableName, whereContent);
		int ICount = this.getJdbcTemplate().queryForObject(countSQL, Integer.class, params);
		page.setTotalCount(ICount);
		//计算多少页 总记录/每页多少条 
		page.setTotalpage((ICount + page.getPageSize() -1) / page.getPageSize());
		sql = getDialect().getPageSQL(page, selectContent, fromContent, whereContent, orderByContent, groupByContent, params);
		List<T> resultList = getList(sql, params);
		page.setResult(resultList);
		return page;
	}


	public List<T> findByProperty(ColDefine colDefine) {

		return this.getList("select * from " + tableName + " where "+ colDefine.getCol() +" " + colDefine.getType() +"'?'" , new Object[]{colDefine.getValue()[0]});
	}
}
