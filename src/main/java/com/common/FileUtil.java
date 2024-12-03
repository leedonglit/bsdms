package com.common;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ThreadUtil;
import com.core.util.Sha256;
import com.isec.util.FileEncryptionUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

public class FileUtil {

//	@SuppressWarnings("resource")
//	public static void download(String file, HttpServletResponse res) throws IOException {
//        // 发送给客户端的数据
//        OutputStream outputStream = res.getOutputStream();
//        byte[] buff = new byte[1024];
//        BufferedInputStream bis = null;
//        // 读取filename
//        bis = new BufferedInputStream(new FileInputStream(new File(file)));
//        int i = bis.read(buff);
//        while (i != -1) {
//            outputStream.write(buff, 0, buff.length);
//            outputStream.flush();
//            i = bis.read(buff);
//        }
//    }
//


	
	/**
	 * 下载项目根目录下doc下的文件
	 * @param response response
	 * @param fileName 文件名
	 * @return 返回结果 成功或者文件不存在
	 */
	public static String downloadFile(HttpServletResponse response, String filePath,String fileName) {
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;		
		try {
			os = response.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (FileNotFoundException e1) {
			return "系统找不到指定的文件";
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "success";
	}

	/**
	 * 文件解密操作
	 * 文件解密需及时文件使用，目前定义10秒后将解密后文件从服务器删除
	 * @param fileAbsolutePath 源文件绝对地址（加密状态的文件地址）
	 * @return 解密后的文件流 File 对象
	 */
	public static File decryptFile(String fileAbsolutePath){
		File f = new File(fileAbsolutePath);
		File targetFile = new File(f.getParent(), "temp");
		if (!targetFile.exists()) targetFile.mkdirs();
		FileEncryptionUtil.decryptFile(fileAbsolutePath,targetFile+File.separator+f.getName(),"HmxIC4oG0Z6QX5gaMhH/bg==");
		File finalF = new File(targetFile+File.separator+f.getName());
		new Thread(()->{
			ThreadUtil.sleep(10000);
			finalF.deleteOnExit();
		}).start();
		return finalF;
	}

	public static String getFileHash(File file){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			FileInputStream inputStream = new FileInputStream(file);
			byte[] byteArray = new byte[1024];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(byteArray)) != -1) {
				digest.update(byteArray, 0, bytesRead);
			}
			inputStream.close();
			byte[] hashBytes = digest.digest();
			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Map<String, String>> batchUpload(MultipartFile[] multipartFiles, String dir) {
		ArrayList<Map<String, String>> list = new ArrayList<>();
		try {
			// 1：遍历获取每个上传的文件
			for (int i = 0; i < multipartFiles.length; i++) {
				MultipartFile multipartFile = multipartFiles[i];
				// 2：获取文件名
				String originalFilename = multipartFile.getOriginalFilename();
				// 3：取文件名后缀
				String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
				// 4：文件重命名
				String fId = UUID.randomUUID().toString();
				String newFileName = fId  + suffix;
				// 5：日期目录
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				String datePath = simpleDateFormat.format(new Date());
				// 6：上传目录
				File targetFile = new File(dir, datePath);
				// 7：如果目录不存在，递归创建
				if (!targetFile.exists()) targetFile.mkdirs();
				// 8：文件上传目录
				File dirFileName = new File(targetFile, newFileName);
				// 9：文件上传
				multipartFile.transferTo(dirFileName);
				FileEncryptionUtil.encryptFile(dirFileName.getAbsolutePath(),dirFileName.getAbsolutePath(),"HmxIC4oG0Z6QX5gaMhH/bg==");
				// 10：路径拼接
				String fileUrl = dir + "/" + datePath + "/" + newFileName;
				// 11：完整访问路径
				Map<String, String> map = new HashMap<>();
				map.put("fileName", originalFilename);// 真实文件名称
				map.put("fileId", fId);// 真实文件名称
				map.put("uuidName", newFileName);// 真实文件名称
				map.put("ext", suffix);// 后缀名
				map.put("fileSize", String.valueOf(multipartFile.getSize()));//
				map.put("dir", dir);
				map.put("filePath", "/" + datePath + "/" + newFileName);//
				map.put("AbsolutePath", fileUrl);//
				map.put("fileHash", getFileHash(dirFileName));//
				System.out.println(map.get("fileHash"));
				list.add(map);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static String encodeFileToBase64Binary(String filePath) {
		try{
			Path path = Paths.get(filePath);
			byte[] bytes = Files.readAllBytes(path);
			return Base64.getEncoder().encodeToString(bytes);
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}

	}
}
