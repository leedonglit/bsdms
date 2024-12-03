package com.core.security.page;

/**
 * 服务业务接口类
 * @author: LiuTao
 * @since:Aug 10, 2012
 * @param <T>
 */
public interface IService<T> {
	/**
	 * 保存一个对象
	 * @param  obj
	 */
	public String save(T obj);
	/**
	 * 保存一个对象  自动生成UUID
	 * @param obj
	 */
	public void saveByAutoUUID(T obj);
	/**
	 * 修改一个对象  通过主键
	 * @param  obj
	 */
	public void update(T  obj);
	/**
	 * 删除一个对象  通过主键
	 * @param  obj
	 */
	public void delete(String pk);
	/**
	 * 获取一个对象  通过主键ID
	 * @param  obj
	 * @return
	 */
	public T getObject(String pk);
	/**
	 * 保存或者修改一个对象集合
	 * @param  obj
	 */
	public void saveOrUpdate(T  obj);
	 
	/**
	 * 没有参数的分页集合
	 * @param page
	 * @return
	 */
	public Page<T> getPage(Page<T> page );
	
	public AutoPage<T> findAutoPage(AutoPage<T> page);
}
