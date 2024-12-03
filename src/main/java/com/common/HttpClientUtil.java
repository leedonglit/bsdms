package com.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpClientUtil {

	public static String requestServlet(String urlString){
		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();  
			urlConnection.setRequestMethod("POST");    
			urlConnection.connect(); 
			InputStream inputStream = urlConnection.getInputStream();  
			String responseStr = ConvertToString(inputStream);  
			return responseStr;
		} catch (Exception e) {
			e.printStackTrace();
			return "";  
		}
	}
	
	public static String requestServlet(String urlString,String params){
		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();  
			urlConnection.setRequestMethod("POST");  
			urlConnection.setDoOutput(true);
			byte[] bypes = params.toString().getBytes();
			urlConnection.getOutputStream().write(bypes);// 输入参数
			urlConnection.connect(); 
			InputStream inputStream = urlConnection.getInputStream();  
			String responseStr = ConvertToString(inputStream);  
			return responseStr;
		} catch (Exception e) {
			e.printStackTrace();
			return "";  
		}
	}
	public static String ConvertToString(InputStream inputStream){  
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);  
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
		StringBuilder result = new StringBuilder();  
		String line = null;  
		try {  
			while((line = bufferedReader.readLine()) != null){  
				result.append(line + "\n");  
			}  
		} catch (IOException e) {  
			e.printStackTrace();  
		} finally {  
			try{  
				inputStreamReader.close();  
				inputStream.close();  
				bufferedReader.close();  
			}catch(IOException e){  
				e.printStackTrace();  
			}  
		}  
		return result.toString();  
	}  

	public static String ConvertToString(FileInputStream inputStream){  
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);  
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
		StringBuilder result = new StringBuilder();  
		String line = null;  
		try {  
			while((line = bufferedReader.readLine()) != null){  
				result.append(line + "\n");  
			}  
		} catch (IOException e) {  
			e.printStackTrace();  
		} finally {  
			try{  
				inputStreamReader.close();  
				inputStream.close();  
				bufferedReader.close();  
			}catch(IOException e){  
				e.printStackTrace();  
			}  
		}  
		return result.toString();  
	}  

}
