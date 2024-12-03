package com.core.security.page;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface IDao<T,PK> {

	/**
	 * 传入一条SQL语句  一个类对象 、 以及相关的查询条件等等 ，返回一个对象的List集合
	 * @param sql  SQL语句
	 * @param cla  类对象 
	 * @param objects 参数
	 * @return  List<Class<?>> 集合
	 */
	public List<?> getListBySql(String sql , Class<?> cla , Object ...objects);
	/**
	 * 传入一条SQL语句  一个类对象 、 以及相关的查询条件等等 ，返回一个对象的List集合
	 * @param sql  SQL语句
	 * @param cla  类对象 
	 * @param objects 参数
	 * @return  List<Class<?>> 集合
	 */
	public List<T> getList(String sql , Class<T> cla , Object ...objects);
	/**
	 * 获取当前对象List 
	 * @param sql SQL语句
	 * @param objects  查询参数
	 * @return
	 */
	public List<T> getList(String sql , Object ...objects);
	/**
	 * 通过单个对象属性查找对象
	 * @param sql SQL语句
	 * @param objects  查询参数
	 * @return
	 */
	public List<T> findByProperty(String propertyName,Object value);
	/**
	 * 传入一条SQL语句  一个类对象 、 以及相关的查询条件等等 ，返回一个对象的对象
	 * @param sql  SQL语句
	 * @param cla  类对象 
	 * @param objects 参数
	 * @return  Object
	 */
	public Object getObjectBySql (String sql , Class<?> cla,Object ...objects);
	/**
	 * 获取当前对象
	 * @param sql SQL 语句 
	 * @param key  主键ID
	 * @return  Object
	 */
	public T getObject (String sql ,Object ...objects);
	/**
	 * 获取当前对象  通过主键ID
	 * @param key  主键ID
	 * @return  Object
	 */
	public T getObject (PK key);
	/**
	 * 保存一个对象
	 * @param object
	 * @return 返回保存后的对象
	 */
	public String saveObject(T object);
	/**
	 * 保存一个对象
	 * @param object
	 * @return 返回保存后的对象
	 */
	public T saveObjectByAutoUUID(T object);
	/**
	 * 保存一组对象集合  暂时只提供的List的集合形式
		 * @param tList
	 */
	public void saveAll(List<T> tList);
	/**
	 * 修改一个对象 并返回该对象 都是通过主键来修改 
	 * @param object
	 * @return T 
	 */
	public T updateObject(T object );
	/**
	 * 批量修改数据  通过主键修改
	 */
	public void updateAll(List<T> tList);
	/**
	 * 执行条SQL语句 用于除了查询业务以外的操作
	 * @param sql SQL 语句
	 * @param objects 参数
	 */
	public void excute(String sql , Object ...objects );
	/**
	 * 批量执行SQL
	 * @param sqls SQL 语句数组
	 */
	public void excute(String[] sqls );
	/**
	 * 通过SQL语句查询得到统计数据信息
	 * @param sql SQL语句
	 * @param objects 参数对象
	 * @return 
	 */
	public Integer getInt(String sql ,Object ...objects);
	/**
	 * 删除一个对象  通过主键ID
	 * @param pk
	 */
	public void deleteObject(PK pk);
   
	/**
	 * 删除一条数据 通过SQL语句形式
	 * @param sql
	 * @param objects
	 */
	public void deleteObjectBySql(String sql , Object ...objects);
	/**
	 * 获取当前对象所有列表集合
	 * @return
	 */
	public List<T> getAllList();
	/**
	 * 删除当前对象所有集合
	 * @return
	 */
	public int deleteAll();
	
	public AutoPage<T> findAutoPage(AutoPage<T> page);
	/**
	 * 批量插入数据库记录
	 * @param tableName
	 * @param list
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public int[] batchInsert(String tableName, final List<T> list) ;
	/**
	 * 批量更新数据库记录
	 * @param list 实体对象的list
	 * @return
	 */
	public int[] batchUpdate(final List<T> list);
	
	public String getPkey();
}
