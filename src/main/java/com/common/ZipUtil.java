package com.common;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	private static final int BUFFER_SIZE = 2 * 1024;

	/**
	 * 压缩成ZIP 方法1 
	 * @param srcDir                  压缩文件夹路径
	 * @param out                     压缩文件输出流
	 * @param KeepDirStructure        是否保留原来的目录结构,
	 *                    true:保留目录结构;
	 *                    false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws RuntimeException
	 *             压缩失败会抛出运行时异常
	 */
	public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure){
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			File sourceFile = new File(srcDir);
			compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 压缩成ZIP 方法2
	 * @param srcFiles       需要压缩的文件列表
	 * @param out            压缩文件输出流
	 * @throws RuntimeException
	 *             压缩失败会抛出运行时异常
	 */
	public static void toZip(List<File> srcFiles, OutputStream out){
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[BUFFER_SIZE];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 压缩成ZIP 方法2	 * 
	 * @param srcFiles        需要压缩的文件列表
	 * @param out             压缩文件输出流
	 * @throws RuntimeException
	 *             压缩失败会抛出运行时异常
	 */
	public static void toZip(File[] srcFiles, OutputStream out){
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[BUFFER_SIZE];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 递归压缩方法
	 * @param sourceFile            源文件
	 * @param zos                   zip输出流
	 * @param name                  压缩后的名称
	 * @param KeepDirStruc
	 *              ture：是否保留原来的目录结构,true:保留目录结构;
	 *              false：所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws Exception
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name,
			boolean KeepDirStructure) {
		byte[] buf = new byte[BUFFER_SIZE];
		try {
			if (sourceFile.isFile()) {
				// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
				zos.putNextEntry(new ZipEntry(name));
				// copy文件到zip输出流中
				int len;
				FileInputStream in = new FileInputStream(sourceFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				// Complete the entry
				zos.closeEntry();
				in.close();
			} else {
				File[] listFiles = sourceFile.listFiles();
				if (listFiles == null || listFiles.length == 0) {
					// 需要保留原来的文件结构时,需要对空文件夹进行处理
					if (KeepDirStructure) {
						// 空文件夹的处理
						zos.putNextEntry(new ZipEntry(name + "/"));
						// 没有文件，不需要文件的copy
						zos.closeEntry();
					}
				} else {
					for (File file : listFiles) {
						// 判断是否需要保留原来的文件结构
						if (KeepDirStructure) {
							// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
							// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
							compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
						} else {
							compress(file, zos, file.getName(), KeepDirStructure);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public static void zipEncipher(File []files,String zipFilePath,String password){
		// 创建ZipFile对象，直接在构造函数中传入密码
		ZipFile zipFile = new ZipFile(zipFilePath, password.toCharArray());
		try {
			// 设置压缩参数
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(CompressionMethod.DEFLATE);
			parameters.setCompressionLevel(CompressionLevel.NORMAL);
			parameters.setEncryptFiles(true);
			parameters.setEncryptionMethod(EncryptionMethod.AES);
			parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
			// 添加文件到ZIP并压缩
			for (int i = 0; i < files.length; i++) {
				zipFile.addFile(files[i], parameters);
			}
			// 可选：测试解压以验证密码
			if (zipFile.isValidZipFile()) {
				System.out.println("The ZIP file is valid.");
			} else {
				System.out.println("The ZIP file is not valid.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				zipFile.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
