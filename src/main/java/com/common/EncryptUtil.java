package com.common;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author ldonglit 加密解密工具类
 */
public class EncryptUtil {

	private static final int RADIX = 16;

	private static final String SEED = "0933910847463829827159347601486730416058";

	public  static final String PASSWORD_ENCRYPTED_PREFIX = "Encrypted "; 


	public static final String encryptPassword(String password){
		if (password==null) return "";
		if (password.length()==0) return "";
		BigInteger bi_passwd = new BigInteger(password.getBytes());
		BigInteger bi_r0  = new BigInteger(SEED);
		BigInteger bi_r1  = bi_r0.xor(bi_passwd);
		return PASSWORD_ENCRYPTED_PREFIX + bi_r1.toString(RADIX); 
	}

	public static final String decryptPassword(String encrypted){
		if (encrypted==null) return "";
		if (encrypted.length()==0) return "";
		BigInteger bi_confuse  = new BigInteger(SEED);
		try
		{
			BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
			BigInteger bi_r0 = bi_r1.xor(bi_confuse);
			return new String(bi_r0.toByteArray()); 
		}
		catch(Exception e)
		{
			return "";
		}
	} 

	/** 
	 * 将byte[]转为各种进制的字符串 
	 * @param bytes byte[] 
	 * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制 
	 * @return 转换后的字符串 
	 */  
	public static String binary(byte[] bytes, int radix){  
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数  
	}  

	/** 
	 * base 64 encode 
	 * @param bytes 待编码的byte[] 
	 * @return 编码后的base 64 code 
	 */  
	public static String base64Encode(byte[] bytes){  
		return Base64.getEncoder().encodeToString(bytes);
	}  

	/** 
	 * base 64 decode 
	 * @param base64Code 待解码的base 64 code 
	 * @return 解码后的byte[] 
	 * @throws Exception 
	 */  
	public static byte[] base64Decode(String base64Code) throws Exception{  
		return Base64.getDecoder().decode(base64Code);
	}  

	/** 
	 * 获取byte[]的md5值 
	 * @param bytes byte[] 
	 * @return md5 
	 * @throws Exception 
	 */  
	public static byte[] md5(byte[] bytes) throws Exception {  
		MessageDigest md = MessageDigest.getInstance("MD5");  
		md.update(bytes);  
		return md.digest();  
	}  

	/** 
	 * 获取字符串md5值 
	 * @param msg  
	 * @return md5 
	 * @throws Exception 
	 */  
	public static byte[] md5(String msg) throws Exception {  
		return md5(msg.getBytes());  
	}  

	/** 
	 * 结合base64实现md5加密 
	 * @param msg 待加密字符串 
	 * @return 获取md5后转为base64 
	 * @throws Exception 
	 */  
	public static String md5Encrypt(String msg) throws Exception{  
		return base64Encode(md5(msg));  
	}  

	/** 
	 * AES加密 
	 * @param content 待加密的内容 
	 * @param encryptKey 加密密钥 
	 * @return 加密后的byte[] 
	 * @throws Exception 
	 */  
	public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception { 
		KeyGenerator kgen = KeyGenerator.getInstance("AES");  
		//防止linux下 随机生成key  
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );     
		secureRandom.setSeed(encryptKey.getBytes());     
		kgen.init(128, secureRandom);  
		Cipher cipher = Cipher.getInstance("AES");  
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
		return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
	}  

	/** 
	 * AES加密为base 64 code 
	 * @param content 待加密的内容 
	 * @param encryptKey 加密密钥 
	 * @return 加密后的base 64 code 
	 * @throws Exception 
	 */  
	public static String aesEncrypt(String content, String encryptKey) throws Exception {  
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}  

	/** 
	 * AES解密 
	 * @param encryptBytes 待解密的byte[] 
	 * @param decryptKey 解密密钥 
	 * @return 解密后的String 
	 * @throws Exception 
	 */  
	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
		KeyGenerator kgen = KeyGenerator.getInstance("AES");  
		kgen.init(128, new SecureRandom(decryptKey.getBytes()));  
		Cipher cipher = Cipher.getInstance("AES");  
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
		byte[] decryptBytes = cipher.doFinal(encryptBytes);  
		return new String(decryptBytes, Charset.defaultCharset());
	}  



	/** 
	 * 将base 64 code AES解密 
	 * @param encryptStr 待解密的base 64 code 
	 * @param decryptKey 解密密钥 
	 * @return 解密后的string 
	 * @throws Exception 
	 */  
	public static String aesDecrypt(String encryptStr, String decryptKey) {  
		try {
			return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
	}  


	public static class Sha1Encrypt {

		private static final int[] abcde = { 
				0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476, 0xc3d2e1f0 
		}; 


		// 摘要数据存储数组 
		private static int[] digestInt = new int[5]; 

		// 计算过程中的临时数据存储数组 
		private static int[] tmpData = new int[80]; 

		// 计算sha-1摘要 
		private static int process_input_bytes(byte[] bytedata) { 
			// 初试化常量 
			System.arraycopy(abcde, 0, digestInt, 0, abcde.length); 
			// 格式化输入字节数组，补10及长度数据 
			byte[] newbyte = byteArrayFormatData(bytedata); 
			// 获取数据摘要计算的数据单元个数 
			int MCount = newbyte.length / 64; 
			// 循环对每个数据单元进行摘要计算 
			for (int pos = 0; pos < MCount; pos++) { 
				// 将每个单元的数据转换成16个整型数据，并保存到tmpData的前16个数组元素中 
				for (int j = 0; j < 16; j++) { 
					tmpData[j] = byteArrayToInt(newbyte, (pos * 64) + (j * 4)); 
				} 
				// 摘要计算函数 
				encrypt(); 
			} 
			return 20; 
		} 

		// 格式化输入字节数组格式 
		private static byte[] byteArrayFormatData(byte[] bytedata) { 
			// 补0数量 
			int zeros = 0; 
			// 补位后总位数 
			int size = 0; 
			// 原始数据长度 
			int n = bytedata.length; 
			// 模64后的剩余位数 
			int m = n % 64; 
			// 计算添加0的个数以及添加10后的总长度 
			if (m < 56) { 
				zeros = 55 - m; 
				size = n - m + 64; 
			} else if (m == 56) { 
				zeros = 63; 
				size = n + 8 + 64; 
			} else { 
				zeros = 63 - m + 56; 
				size = (n + 64) - m + 64; 
			} 
			// 补位后生成的新数组内容 
			byte[] newbyte = new byte[size]; 
			// 复制数组的前面部分 
			System.arraycopy(bytedata, 0, newbyte, 0, n); 
			// 获得数组Append数据元素的位置 
			int l = n; 
			// 补1操作 
			newbyte[l++] = (byte) 0x80; 
			// 补0操作 
			for (int i = 0; i < zeros; i++) { 
				newbyte[l++] = (byte) 0x00; 
			} 
			// 计算数据长度，补数据长度位共8字节，长整型 
			long N = (long) n * 8; 
			byte h8 = (byte) (N & 0xFF); 
			byte h7 = (byte) ((N >> 8) & 0xFF); 
			byte h6 = (byte) ((N >> 16) & 0xFF); 
			byte h5 = (byte) ((N >> 24) & 0xFF); 
			byte h4 = (byte) ((N >> 32) & 0xFF); 
			byte h3 = (byte) ((N >> 40) & 0xFF); 
			byte h2 = (byte) ((N >> 48) & 0xFF); 
			byte h1 = (byte) (N >> 56); 
			newbyte[l++] = h1; 
			newbyte[l++] = h2; 
			newbyte[l++] = h3; 
			newbyte[l++] = h4; 
			newbyte[l++] = h5; 
			newbyte[l++] = h6; 
			newbyte[l++] = h7; 
			newbyte[l++] = h8; 
			return newbyte; 
		} 
		private static int f1(int x, int y, int z) { 
			return (x & y) | (~x & z); 
		} 
		private static int f2(int x, int y, int z) { 
			return x ^ y ^ z; 
		} 
		private static int f3(int x, int y, int z) { 
			return (x & y) | (x & z) | (y & z); 
		} 
		private static int f4(int x, int y) { 
			return (x << y) | x >>> (32 - y); 
		} 

		// 单元摘要计算函数 
		private static void encrypt() { 
			for (int i = 16; i <= 79; i++) { 
				tmpData[i] = f4(tmpData[i - 3] ^ tmpData[i - 8] ^ tmpData[i - 14] ^ 
						tmpData[i - 16], 1); 
			} 
			int[] tmpabcde = new int[5]; 
			for (int i1 = 0; i1 < tmpabcde.length; i1++) { 
				tmpabcde[i1] = digestInt[i1]; 
			} 
			for (int j = 0; j <= 19; j++) { 
				int tmp = f4(tmpabcde[0], 5) + 
						f1(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + 
						tmpData[j] + 0x5a827999; 
				tmpabcde[4] = tmpabcde[3]; 
				tmpabcde[3] = tmpabcde[2]; 
				tmpabcde[2] = f4(tmpabcde[1], 30); 
				tmpabcde[1] = tmpabcde[0]; 
				tmpabcde[0] = tmp; 
			} 
			for (int k = 20; k <= 39; k++) { 
				int tmp = f4(tmpabcde[0], 5) + 
						f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + 
						tmpData[k] + 0x6ed9eba1; 
				tmpabcde[4] = tmpabcde[3]; 
				tmpabcde[3] = tmpabcde[2]; 
				tmpabcde[2] = f4(tmpabcde[1], 30); 
				tmpabcde[1] = tmpabcde[0]; 
				tmpabcde[0] = tmp; 
			} 
			for (int l = 40; l <= 59; l++) { 
				int tmp = f4(tmpabcde[0], 5) + 
						f3(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + 
						tmpData[l] + 0x8f1bbcdc; 
				tmpabcde[4] = tmpabcde[3]; 
				tmpabcde[3] = tmpabcde[2]; 
				tmpabcde[2] = f4(tmpabcde[1], 30); 
				tmpabcde[1] = tmpabcde[0]; 
				tmpabcde[0] = tmp; 
			} 
			for (int m = 60; m <= 79; m++) { 
				int tmp = f4(tmpabcde[0], 5) + 
						f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + 
						tmpData[m] + 0xca62c1d6; 
				tmpabcde[4] = tmpabcde[3]; 
				tmpabcde[3] = tmpabcde[2]; 
				tmpabcde[2] = f4(tmpabcde[1], 30); 
				tmpabcde[1] = tmpabcde[0]; 
				tmpabcde[0] = tmp; 
			} 
			for (int i2 = 0; i2 < tmpabcde.length; i2++) { 
				digestInt[i2] = digestInt[i2] + tmpabcde[i2]; 
			} 
			for (int n = 0; n < tmpData.length; n++) { 
				tmpData[n] = 0; 
			} 
		} 

		// 4字节数组转换为整数 
		private static int byteArrayToInt(byte[] bytedata, int i) { 
			return ((bytedata[i] & 0xff) << 24) | ((bytedata[i + 1] & 0xff) << 16) | 
					((bytedata[i + 2] & 0xff) << 8) | (bytedata[i + 3] & 0xff); 
		} 

		// 整数转换为4字节数组 
		private static void intToByteArray(int intValue, byte[] byteData, int i) { 
			byteData[i] = (byte) (intValue >>> 24); 
			byteData[i + 1] = (byte) (intValue >>> 16); 
			byteData[i + 2] = (byte) (intValue >>> 8); 
			byteData[i + 3] = (byte) intValue; 
		} 

		// 将字节转换为十六进制字符串 
		private static String byteToHexString(byte ib) { 
			char[] Digit = { 
					'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 
					'D', 'E', 'F' 
			}; 
			char[] ob = new char[2]; 
			ob[0] = Digit[(ib >>> 4) & 0X0F]; 
			ob[1] = Digit[ib & 0X0F]; 
			String s = new String(ob); 
			return s; 
		} 
		// 将字节数组转换为十六进制字符串 
		private static String byteArrayToHexString(byte[] bytearray) { 
			String strDigest = ""; 
			for (int i = 0; i < bytearray.length; i++) { 
				strDigest += byteToHexString(bytearray[i]); 
			} 
			return strDigest; 
		} 
		// 计算sha-1摘要，返回相应的字节数组 
		public static byte[] getDigestOfBytes(byte[] byteData) { 
			process_input_bytes(byteData); 
			byte[] digest = new byte[20]; 
			for (int i = 0; i < digestInt.length; i++) { 
				intToByteArray(digestInt[i], digest, i * 4); 
			} 
			return digest; 
		} 

		// 计算sha-1摘要，返回相应的十六进制字符串 
		public static String getSha1Encrypt(String byteData) { 

			return byteArrayToHexString(getDigestOfBytes(byteData.getBytes())); 
		}  

		public static void main(String[] args) {
			System.err.println(getSha1Encrypt("ncepu123456"));
			System.err.println(getSha1Encrypt("stu666"));
		}
	}
}
