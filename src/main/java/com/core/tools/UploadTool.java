package com.core.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 上传工具类
 * @since 2018-11-06
 * @author ldonglit
 *
 */
public class UploadTool {
	
	private static Map<String, String> fileMap = null;
	
	private static String filePath = null;

	/**
	 * 文件上传方法（多文件）
	 * @param file
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public static List<Map<String, String>> uploadFile(HttpServletRequest request, String moduleName) { 
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
		Iterator<String> files = multipartHttpServletRequest.getFileNames();
		List<Map<String, String>> ls= new ArrayList<Map<String,String>>();
		while (files.hasNext()) {
			MultipartFile multipartFile = multipartHttpServletRequest.getFile(files.next());
			upload(multipartFile,moduleName);
			ls.add(fileMap);
		} 
		return ls;
	}
	
	private static void upload(MultipartFile file,String moduleName) {
		try {
			if (filePath == null) {
				filePath = PropertiesTool.getString("upload.file.rpath").
						concat(File.separator).
						concat(moduleName).
						concat(File.separator);
			}
			fileMap = new HashMap<String, String>();
			File targetFile = new File(filePath);  
			if(!targetFile.exists()){    
				targetFile.mkdirs();    
			}       
			String realName = file.getOriginalFilename();
			String extName = realName.substring(realName.lastIndexOf("."));//文件后缀
			String fName = UUID.randomUUID().toString().concat(extName);
			fileMap.put("realFileName", realName);//文件真实名称
			fileMap.put("path", filePath+fName);//文件存储路径
			fileMap.put("size", file.getSize()+"");//文件大小
			fileMap.put("uuidName", fName);//文件存储名称
			fileMap.put("url", PropertiesTool.getString("static.file.url").concat(File.separator).concat(moduleName).concat(File.separator).concat(fName));//网络请求地址
			String contentType = file.getContentType();
			fileMap.put("fileType", contentType);//文件类型
			fileMap.put("absolutePath", filePath);//文件绝对路径
			FileOutputStream out = new FileOutputStream(filePath+fName);
			out.write(file.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public   String getUploadUrl() {
		String upUrl="";
		
		String property = PropertiesTool.getString("spring.resources.static-locations");
			String[] split = property.split(",");
			
			for (String url : split) {
				if(url.contains("file")) {
					int indexOf = url.indexOf(":");
					 upUrl = url.substring(indexOf+1,url.length());
				}
			}
		return upUrl;
		
	}
}