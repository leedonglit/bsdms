package com.core.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import org.springframework.util.ClassUtils;

public class SyspathTool {


	/**
	 * 获取项目根路径
	 * @return
	 */
	public static String getSysGenPath(){
		ClassLoader classLoaderToUse = ClassUtils.getDefaultClassLoader();
		return classLoaderToUse.getResource("").getPath();
	}

	/**
	 * 获取项目相对路径
	 * @return
	 */
	public static String getSysRelaPath() {
		ClassLoader classLoaderToUse = ClassUtils.getDefaultClassLoader();
		return classLoaderToUse.getResource("").toString();
	}


	/**
	 * 根据文件名称获取当前文件所在路径
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String getFilePath(String fileName) {
		ClassLoader classLoaderToUse = ClassUtils.getDefaultClassLoader();
		Enumeration<URL> urls = null;
		try {
			urls = (classLoaderToUse != null ? classLoaderToUse.getResources(fileName) :
				ClassLoader.getSystemResources(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urls.nextElement().toString();

	}
 
	/**
	 * 根据文件名称得到文件流
	 * @param fileName
	 * @return
	 * @throws URISyntaxException 
	 */
	public static InputStream loadFile(String fileName) throws URISyntaxException{
		ClassLoader classLoaderToUse = ClassUtils.getDefaultClassLoader();
		Enumeration<URL> urls = null;
		try {
			urls = (classLoaderToUse != null ? classLoaderToUse.getResources(fileName) :
				ClassLoader.getSystemResources(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return  urls.nextElement().openConnection().getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	 
}
