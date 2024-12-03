package com.core.extend.IO;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 文件操作工具类
 * 文件打包
 * 文件拷贝
 * 文件删除
 * 文件解压
 * @author LDL
 *
 */
public class IOTools {

	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("删除文件失败:" + fileName + "不存在！");
			return false;
		} else {
			if (file.isFile())
				return deleteFile(fileName);
			else
				return deleteDirectory(fileName);
		}
	}


	/**
	 * 删除单个文件
	 * @param fileName 文件全路径
	 * @return 删除结果
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 删除目录
	 * @param dir 目录路径
	 * @return 删除结果
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				if (!flag)
					break;
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i]
						.getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 文件拷贝
	 * @param src 当前文件全路径
	 * @param dest 拷贝目标目录
	 * @throws IOException
	 */
	public void copyFile(String src,String dest) throws IOException{
		FileInputStream in=new FileInputStream(src);
		File file=new File(dest);
		if(!file.exists())
			file.createNewFile();
		FileOutputStream out=new FileOutputStream(file);
		int c;
		byte buffer[]=new byte[1024];
		while((c=in.read(buffer))!=-1){
			for(int i=0;i<c;i++)
				out.write(buffer[i]);        
		}
		in.close();
		out.close();
	}

	/**
	 * 文件打压缩包
	 * @param files 文件集合
	 * @param outputStream 输出文件流
	 */
	public static void zipFile(List<File> files, ZipOutputStream outputStream) {
		int size = files.size();
		for (int i = 0; i < size; i++) {
			File file = (File) files.get(i);
			zipFile(file, outputStream);
		}
	}


	/**
	 * 单文件打压缩
	 * @param inputFile
	 * @param ouputStream
	 */
	public static void zipFile(File inputFile, ZipOutputStream ouputStream) {
		try {
			if (inputFile.exists()) {
				if (inputFile.isFile()) {
					FileInputStream IN = new FileInputStream(inputFile);
					BufferedInputStream bins = new BufferedInputStream(IN, 512);
					ZipEntry entry = new ZipEntry(inputFile.getName());
					ouputStream.putNextEntry(entry);
					// 向压缩文件中输出数据
					int nNumber;
					byte[] buffer = new byte[512];
					while ((nNumber = bins.read(buffer)) != -1) {
						ouputStream.write(buffer, 0, nNumber);
					}
					// 关闭创建的流对象
					bins.close();
					IN.close();
				} else {
					try {
						File[] files = inputFile.listFiles();
						for (int i = 0; i < files.length; i++) {
							zipFile(files[i], ouputStream);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 获取目录下所有文件集合
	 * @param path 目录路径
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<File> getFiles(String path) throws Exception {
		//目标集合fileList
		ArrayList<File> fileList = new ArrayList<File>();
		File file = new File(path);
		if(file.isDirectory()){
			File []files = file.listFiles();
			for(File fileIndex:files){
				//如果这个文件是目录，则进行递归搜索
				if(fileIndex.isDirectory()){
					getFiles(fileIndex.getPath());
				}else {
					//如果文件是普通文件，则将文件句柄放入集合中
					fileList.add(fileIndex);
				}
			}
		}
		return fileList;
	}
}
