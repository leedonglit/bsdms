package com.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class ExprotWord {
	
	private Configuration configuration = null;

	public ExprotWord() {
		configuration = new Configuration(new Version("2.3.28"));
		configuration.setDefaultEncoding("UTF-8");
	}
 
	public void createWord(Map<String, Object> dataMap,String path,String tName,File file) throws Exception {
		//template = tName;
		configuration.setClassForTemplateLoading(this.getClass(), path); // FTL文件所存在的位置
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		Template t = null;
		try {
			t = configuration.getTemplate(tName); // 文件名
		} catch (IOException e) {
			e.printStackTrace();
		}
		//File outFile = new File("G:/outFiles" + Math.random() * 10000 + ".doc");
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			// 必须加 "utf-8" 不然在 UserInfoRestController 通过webservice的方式调用 生成的文件 打开
			// 不了，会报错（而当做java程序去运行，则没有"utf-8"生成的文件也能正常打开的），原因不详
			// 规范编写应该是要加上文件的编码的"utf-8"
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			t.process(dataMap, out);
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	} 

	public String getImageStr(File file) {
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(file);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String imageCodeBase64 = Base64.encodeBase64String(data);
		return imageCodeBase64;
	}

	/*private String getImageStrone() {
		String imgFile = "G:\\Users\\9.jpg";
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// BASE64Encoder encoder = new BASE64Encoder();
		// return encoder.encode(data);
		// return data != null ? encoder.encode(data) : "";
		// System.out.println(new String(data));
		//这里用到将图片转为base64码，具体看官方解释
		String imageCodeBase64 = Base64.encodeBase64String(data);
		return imageCodeBase64;
	}*/
}
