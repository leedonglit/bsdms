package com.core.util;

import java.io.IOException;
import java.io.OutputStream;

import com.core.extend.validatecode.ChineseCaptcha;
import com.core.extend.validatecode.ChineseGifCaptcha;
import com.core.extend.validatecode.GifCaptcha;
import com.core.extend.validatecode.VerifyCodeUtil;

/**
 * 验证码生成工具类
 * @author ldonglit
 *
 */
public class VCodeUtil {

	//生成验证码
	public String createCode() throws IOException {
		switch (codeType) {
		case 2:
			GifCaptcha captcha = new GifCaptcha(this.width, this.height ,this.codeLengh);
			captcha.out(outputStream);
			return captcha.text();
		case 3:
			ChineseCaptcha chineseCaptcha = new ChineseCaptcha(this.width, this.height ,this.codeLengh);
			chineseCaptcha.out(outputStream);
			return chineseCaptcha.text();
		case 4:
			ChineseGifCaptcha chineseGifCaptcha = new ChineseGifCaptcha(this.width, this.height ,this.codeLengh);
			chineseGifCaptcha.out(outputStream);
			return chineseGifCaptcha.text();
		default:
			return VerifyCodeUtil.outputVerifyImage(width,height,outputStream,codeLengh);
		}
	}
	
	private int width = 140;

	private int height = 40;

	private int codeLengh = 5;

	private OutputStream outputStream = null;

	/**
	 * 1:静态数字英文混合
	 * 2:动态数字英文混合
	 * 3:静态中文
	 * 4:动态中文
	 */
	private int codeType = 1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getCodeLengh() {
		return codeLengh;
	}

	public void setCodeLengh(int codeLengh) {
		this.codeLengh = codeLengh;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
 
	public int getCodeType() {
		return codeType;
	}

	public void setCodeType(int codeType) {
		this.codeType = codeType;
	}

	
}
