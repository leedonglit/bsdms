package com.isec.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class FileEncryptionUtil {


    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";


    public static void encryptFile(String input,String out,String key){
        doCrypto(Cipher.ENCRYPT_MODE,input,out,key);
    }

    public static void decryptFile(String input,String out,String key){
        doCrypto(Cipher.DECRYPT_MODE,input,out,key);
    }

    public static void doCrypto(int cipherMode,String input,String out,String key){
        try{
            Key secretKey = new SecretKeySpec(key.getBytes(),ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode,secretKey);
            FileInputStream inputStream = new FileInputStream(input);
            byte[] inputBytes = new byte[(int) new File(input).length()];
            inputStream.read(inputBytes);
            byte[] outBytes = cipher.doFinal(inputBytes);
            FileOutputStream outputStream = new FileOutputStream(out);
            outputStream.write(outBytes);
            inputStream.close();;
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 加密
     * @param content 要加密的字符串
     * @param encryptKey 加密的密钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String encryptKey){
        try {
            KeyGenerator kGen = KeyGenerator.getInstance("AES");
            kGen.init(128);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
            byte[] b = cipher.doFinal(content.getBytes("utf-8"));
            //采用base64算法进行转码，避免出现中文乱码
            return Base64.encodeBase64String(b);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


//    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
////        System.out.println(encrypt("123","1234abcdef567890"));
////        encryptFile("E:\\360MoveData\\Users\\Administrator\\Desktop\\微信图片_20240812155810.jpg","E:\\360MoveData\\Users\\Administrator\\Desktop\\1.jpg","gCTJw5JI4+b8uBRpN+tedg==");
////        decryptFile("D:\\tools\\ideaIU-2022.2.3\\IntelliJ IDEA 2022.2.3\\workspace\\Academic\\upload\\20241109\\3c69ed55-e76e-4c2b-971a-1b1446e1a0cb.xlsx","D:\\tools\\ideaIU-2022.2.3\\IntelliJ IDEA 2022.2.3\\workspace\\Academic\\upload\\20241109\\3c69ed55-e76e-4c2b-971a-1b1446e1a0cb.xlsx","HmxIC4oG0Z6QX5gaMhH/bg==");
//        File f = new File("D:\\tools\\ideaIU-2022.2.3\\IntelliJ IDEA 2022.2.3\\workspace\\Academic\\upload\\20241109\\3c69ed55-e76e-4c2b-971a-1b1446e1a0cb.xlsx");
//        System.out.println(f.getParent());
//    }


}
