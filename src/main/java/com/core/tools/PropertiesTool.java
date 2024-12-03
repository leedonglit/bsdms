package com.core.tools;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * java 配置文件操作工具类
 * @since 2018-11-06
 * @author ldonglit
 *
 */
public class PropertiesTool {

	public static Map<String, String> propertiesMap = new HashMap<String, String>();

	private static void processProperties(Properties props) throws BeansException {
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			try {
				// PropertiesLoaderUtils的默认编码是ISO-8859-1,在这里转码一下
				propertiesMap.put(keyStr, new String(props.getProperty(keyStr).getBytes("ISO-8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadAllProperties(String propertyFileName) {
		try {
			Properties properties = PropertiesLoaderUtils.loadAllProperties(propertyFileName);
			processProperties(properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取int类型值
	 * @param name
	 * @return
	 */
	public static Integer getInteger(String name) {
		return Integer.valueOf(getString(name));
	}

	/**
	 * 获取boolean
	 * @param name
	 * @return
	 */
	public static Boolean getBoolean(String name) {
		return Boolean.valueOf(getString(name));
	}

	/**
	 * 获取string类型
	 * @param name
	 * @return
	 */
	public static String getString(String name) {
		return propertiesMap.get(name).toString();
	}

	/**
	 * 获取集合
	 * @param name
	 * @return
	 */
	public static String[] getStrings(String name) {
		return getString(name).split(",");
	}

	public static Map<String, String> getAllProperty() {
		return propertiesMap;
	}
}
