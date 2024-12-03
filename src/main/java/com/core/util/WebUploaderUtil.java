package com.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.FileCopyUtils;

import com.core.tools.PropertiesTool;

/**
 * webuploader上传
 * @since 2019-7-05
 */
public class WebUploaderUtil {
	
	//private static Map<String, String> fileMap = null;
	
	//@Param moudleName 上传文件的要放入的文件夹名字
	public static Map<String,String> upload(HttpServletRequest request,String moudleName) throws Exception{
		Map<String, String> fileMap =new HashMap<String, String>();
		String name = request.getParameter("name");
		String type = name.substring(name.lastIndexOf(".") + 1 );
		String uuid = UUID.randomUUID().toString();
		name = uuid.concat("."+type);
		
		String path = PropertiesTool.getString("upload.file.rpath").
				concat(File.separator).
				concat(moudleName).
				concat(File.separator);
		ServletInputStream inputStream = request.getInputStream();
		File uploadFile = new File(path);
		String relPathString = uploadFile.getPath();
		if(!uploadFile.exists()){    
			uploadFile.mkdirs();    
		}
		FileCopyUtils.copy(inputStream, new FileOutputStream(uploadFile+"/"+name));
		inputStream.close();
		fileMap.put("realFileName", request.getParameter("name"));//文件真实名称
		fileMap.put("path", relPathString);//文件存储路径
		fileMap.put("size", request.getParameter("size"));//文件大小
		fileMap.put("uuidName", name);//文件存储名称
		fileMap.put("url", PropertiesTool.getString("static.file.url").concat(File.separator).concat(moudleName).concat(File.separator).concat("upload"));//网络请求地址
		fileMap.put("fileType", type);//文件类型
		fileMap.put("absolutePath", path);//文件绝对路径
		fileMap.put("fileId", uuid);//文件存储id
		
		return fileMap;
}

	public static Map<String, String> uploadUserImg(HttpServletRequest request, String string, String user_id) throws IOException {
		Map<String, String> fileMap =new HashMap<String, String>();
		String name = request.getParameter("name");
		String type = name.substring(name.lastIndexOf(".") + 1 );
		String uuid = user_id;
		name = uuid.concat("."+type);
		
		String path = PropertiesTool.getString("upload.file.rpath").
				concat(File.separator).
				concat(string).
				concat(File.separator);
		ServletInputStream inputStream = request.getInputStream();
		File uploadFile = new File(path);
		String relPathString = uploadFile.getPath();
		fileMap = new HashMap<String, String>();
		if(!uploadFile.exists()){    
			uploadFile.mkdirs();    
		}
		FileCopyUtils.copy(inputStream, new FileOutputStream(uploadFile+"/"+name));
		inputStream.close();
		fileMap.put("realFileName", request.getParameter("name"));//文件真实名称
		fileMap.put("path", relPathString);//文件存储路径
		fileMap.put("size", request.getParameter("size"));//文件大小
		fileMap.put("uuidName", name);//文件存储名称
		fileMap.put("url", File.separator.concat(string).concat(File.separator).concat(name));//网络请求地址
		fileMap.put("fileType", type);//文件类型
		fileMap.put("absolutePath", path);//文件绝对路径
		fileMap.put("fileId", uuid);//文件存储id
		
		return fileMap;
	}
}
