package com.core.security.database.jdbc.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DTO {
	/**
	 * 表名
	 * @return
	 */
	String tableName() default "";
	/**
	 * 主键
	 * @return
	 */
	String pkey() default "id";


	/**
	 * 排序字段
	 * @return
	 */
	String orderby() default "id";


	/**
	 * 树型结构时的上级值
	 * @return
	 */
	String pid()    default "pid";
	/**
	 * 查询时查询的列
	 * @return
	 */
	String select() default "*";

	/**
	 * 是否为自动增长主键
	 * @return
	 */
	boolean isNumAutoKey() default false;
}