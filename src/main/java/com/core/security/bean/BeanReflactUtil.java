package com.core.security.bean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

import com.core.security.database.jdbc.annotation.DtoUtils;
public class BeanReflactUtil {

	public static String getPkColumnName(Class<?> claz) {
		/** */
		String filedName="";
		/** */
		/** 得到bean的所有成员变量 */
		Field[] fileds = claz.getDeclaredFields();
		for (Field field : fileds) {
			// 获取有注解Column的进行转换
			if(field.getAnnotation(Column.class)!=null&&field.isAnnotationPresent(javax.persistence.Id.class)&&!Modifier.isStatic(field.getModifiers())){
				String column=field.getAnnotation(Column.class).name();
				if (column!=null&&!column.equals("")) {
					filedName=column;
				}
			}
		}
		return filedName;
	}

	public static Map<String, Object> bean2Map(Object bean) {

		return bean2Map4NoAnnotation(bean);
	}

	public static Map<String, Object> beanAnnotation2Map(Object bean) {
		return bean2Map4Annotation(bean);
	}

	public static Map<String, Object> bean2Map4NoAnnotation(Object bean) {
		Map<String, Object> fieldNVMap = new HashMap<String, Object>();
		/** */
		/** 如果bean参数的值为null,直接返回 */
		if (bean == null) {
			return fieldNVMap;
		}
		Class<?> claz = bean.getClass();
		/** */
		/** 得到bean的所有成员变量 */
		Field[] fileds = claz.getDeclaredFields();
		for (Field field : fileds) {
			// 判断没有@Transient注解的属性才进行map的转换
			if (!field.isAnnotationPresent(Transient.class)&&!Modifier.isStatic(field.getModifiers())) {
				String fieldName = field.getName();
				String getterMethodName = getGetterOrSetterName(fieldName, "get");
				Object filedValue = null;
				try {
					/** */
					/** 根据成员变量得到该变量的Getter方法 */
					Method method = claz.getMethod(getterMethodName);
					if (method != null) {
						/** */
						/** 根据该成员变量的Getter方法得到该方法的值 */
						filedValue = method.invoke(bean, new Object[0]);
					}
					fieldNVMap.put(fieldName, filedValue);
				} catch (Exception ex) {
				}
			} else {

			}
		}
		return fieldNVMap;
	}

	/**
	 * 根据提供的Object对象，获取object对象有用的信息 是对getFieldMethodList方法的过滤 list(0)
	 * 存的是String类型的FieldName ,list(1) 存的是Field的类型 list(2)
	 * 存的是该Field的getter方法,list(3) 存的是该Field的值
	 * 
	 * @param obj
	 * @return
	 */
	public static Queue<List<Object>> getValidateField(Object obj) {
		if (obj == null)
			return null;
		Queue<List<Object>> _queue = new LinkedList<List<Object>>();
		List<List<Object>> list = getFieldMethodList(obj);
		DtoUtils dto = DtoUtils.getDtoInfo(obj.getClass());
		List<Object> content = new ArrayList<Object>();
		for (List<Object> loc_list : list) {
			if (loc_list.get(3) != null) {
				if (!dto.pkey().equalsIgnoreCase(
						loc_list.get(0).toString().trim())) {
					_queue.offer(loc_list);
				} else {
					content = loc_list;
				}
			}
		}
		_queue.offer(content);
		return _queue;
	}
	/**
	 * 把Bean对象的属性获取注解和值映射成一个MAP 过滤掉添加了@Transient注解的属性字段 用于生产SQL语句时不再进行无关属性的拼接操作.
	 * 
	 * @see com.core.tools.reflect.BeanReflectUtil <BR>
	 * @param bean
	 * @return
	 * @return: Map<String,Object>
	 * @Author: rhr <BR>
	 * @Datetime：2014-6-16 上午11:15:10 <BR>
	 */
	public static Map<String, Object> bean2Map4Annotation(Object bean) {
		Map<String, Object> fieldNVMap = new HashMap<String, Object>();
		/** */
		/** 如果bean参数的值为null,直接返回 */
		if (bean == null) {
			return fieldNVMap;
		}
		Class<?> claz = bean.getClass(); 
		/** */
		/** 得到bean的所有成员变量 */
		Field[] fileds = claz.getDeclaredFields();
		for (Field field : fileds) {
			// 判断没有@Transient注解的属性才进行map的转换
			if (!field.isAnnotationPresent(Transient.class)&&!Modifier.isStatic(field.getModifiers())) {
				// 获取有注解Column的进行转换
				String column="";
				try {
					column = field.getAnnotation(Column.class).name();
				} catch (Exception e) {
					//					System.out.println("字段"+field+"无注解");
					e.printStackTrace();
				}
				if (column!=null&&!column.equals("")) {
					//获取注解里的主键字段，验证主键生成策略
					boolean ifKey=field.isAnnotationPresent(javax.persistence.Id.class);
					String fieldName = field.getName();
					String columnName = field.getAnnotation(Column.class).name();
					String getterMethodName = getGetterOrSetterName(fieldName,
							"get");
					Object filedValue = null;
					try {
						/** */
						/** 根据成员变量得到该变量的Getter方法 */
						Method method = claz.getMethod(getterMethodName);
						if (method != null) {
							/** 根据该成员变量的Getter方法得到该方法的值 */
							filedValue = method.invoke(bean, new Object[0]);
						}
						//如果是主键，且主键的值是空 则验证主键生成策略，
						if(ifKey&&(filedValue==null||filedValue.equals(""))){
							boolean ifGenerated=field.isAnnotationPresent(javax.persistence.GeneratedValue.class);
							//获取主键生成策略
							if(ifGenerated){
								GenerationType generatorType = field.getAnnotation(GeneratedValue.class).strategy();
								if(generatorType.name().equals(GenerationType.IDENTITY.name())){
									//序列 MySQL, SQL Server, DB2, Derby, Sybase, PostgreSQL
								}else if(generatorType.name().equals(GenerationType.SEQUENCE.name())){
									//seq Oracle、PostgreSQL、DB2
									//因jpa seq 配置SequenceGenerator会自动生成seq，比较麻烦，改为自己在数据库建立，只用到seq名称
									String generatorVal = field.getAnnotation(GeneratedValue.class).generator();
									fieldNVMap.put(columnName, generatorVal+".nextval()");
								}else if(generatorType.name().equals(GenerationType.TABLE.name())){
									//暂时不支持
								}else if(generatorType.name().equals(GenerationType.AUTO.name())){
									//uuid
									String uuid = java.util.UUID.randomUUID().toString();
									fieldNVMap.put(columnName, uuid);
								}
							}else{
								String uuid = java.util.UUID.randomUUID().toString();
								fieldNVMap.put(columnName, uuid);
							}
						}else{
							fieldNVMap.put(columnName, filedValue);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} else {

			}
		}
		return fieldNVMap;
	}


	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object object,
			final String fieldName, final Object value) {
		try {
			Field field = getDeclaredField(object, fieldName);
			makeAccessible(field);
			if (field == null) {
				return;
			}
			field.set(object, value);
		} catch (Exception e) {

		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	protected static Field getDeclaredField(final Object object, final String fieldName) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 强行设置Field可访问.
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * Description:[根据成员变量名,得到成员变量的Getter或Setter方法名]
	 * Created by [Huyvanpull] [Jul 6, 2009]
	 * Midified by [修改人] [修改时间]
	 * @param fieldName
	 * @return
	 */
	static private String getGetterOrSetterName(String fieldName, String prdfix) {
		StringBuffer getterOrSetterName = new StringBuffer();
		getterOrSetterName.append(prdfix);
		getterOrSetterName.append(fieldName.substring(0, 1).toUpperCase());
		if (getterOrSetterName.length() > 1) {
			getterOrSetterName.append(fieldName.substring(1));
		}
		return getterOrSetterName.toString();
	}

	/**
	 * 返回一个类的FieldName，FieldType，getterField,FieldValue方法，内层list list(0)
	 * 存的是String类型的FieldName ,list(1) 存的是Field的类型 list(2)
	 * 存的是该Field的getter方法,list(3) 存的是该Field的值，若无值则为null
	 * @param obj
	 * @return
	 */
	public static List<List<Object>> getFieldMethodList(Object obj) {
		if(obj == null){
			return null;
		}
		List<List<Object>> list = new ArrayList<List<Object>>();
		Field[] field = obj.getClass().getDeclaredFields();
		for(Field f : field){
			//判断没有@Transient注解的属性才进行map的转换
			if(!f.isAnnotationPresent(Transient.class)&&!Modifier.isStatic(f.getModifiers())){
				List<Object> _list = new ArrayList<Object>();
				Method[] mth = obj.getClass().getDeclaredMethods();
				for(Method m : mth){
					//if(m.getName().contains("get") && (m.getName().contains(f.getName()))){
					if(m.getName().contains("get") && (m.getName().equals("get"+f.getName()))){
						_list.add(f.getName());
						_list.add(f.getType());
						_list.add(m);
						try {
							_list.add(m.invoke(obj));
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
				list.add(_list);
			}	
		}
		return list;
	}

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
	 * extends HibernateDao<User>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class<?> clazz) {
		return (Class<T>) getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be
	 *         determined
	 */
	public static Class<?> getSuperClassGenricType(@SuppressWarnings("rawtypes") final Class clazz,
			final int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class<?>) params[index];
	}

	public static <T> String getPkFiledName(Class<T> claz) {
		/** */
		String filedName="";
		/** */
		/** 得到bean的所有成员变量 */
		Field[] fileds = claz.getDeclaredFields();
		for (Field field : fileds) {
			// 获取有注解Column的进行转换
			if(field.getAnnotation(Column.class)!=null&&field.isAnnotationPresent(javax.persistence.Id.class)&&!Modifier.isStatic(field.getModifiers())){
				String column=field.getAnnotation(Column.class).name();
				if (column!=null&&!column.equals("")) {
					filedName=field.getName();
				}
			}
		}
		return filedName;
	}
}
