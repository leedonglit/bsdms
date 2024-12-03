package com.core.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.imageio.ImageIO;


public class ImageProcess {

	//图片
	private Image img;
	//宽度
	private int width;
	//高度
	private int height;
	//文件格式
	private String imageFormat;

	//构造函数
	public ImageProcess(InputStream in,String fileName) throws IOException {
		//构造Image对象
		img = ImageIO.read(in);
		//得到源图宽
		width = img.getWidth(null);
		//得到源图长
		height = img.getHeight(null);
		//文件格式
		imageFormat = fileName.substring(fileName.lastIndexOf(".")+1);
	}
	/*
	 * 按照宽度还是高度进行压缩
	 */
	public byte[] resizeFix(int w,int h) throws IOException {
		if (width / height > w / h) {
			return resizeByWidth(w);
		}else {
			return resizeByHeight(h);
		}
	}
	//以宽度为基准，等比例方式图片
	private byte[] resizeByWidth(int w) throws IOException {
		int h = height * w /width;
		return resize(w,h);
	}
	//以宽度为基准，等比例方式图片
	private byte[] resizeByHeight(int h) throws IOException {
		int w = width * h /height;
		return resize(w,h);
	}
	//强制压缩/放大图片到固定的大小
	private byte[] resize(int w, int h) throws IOException {
		//SCALE_SMOOTH 的缩略算法
		BufferedImage image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		//绘制缩小后的图
		image.getGraphics().drawImage(img, 0, 0, w,h,null);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, imageFormat, baos);
		return baos.toByteArray();
	}


	public static String createImg(String content) {
		ByteArrayOutputStream baos = null;
		try {
			BufferedImage bi = new BufferedImage(200, 50, BufferedImage.TYPE_INT_RGB);// INT精确度达到一定,RGB三原色，高度70,宽度150
			Font titleFont = new Font("STXingkai", Font.PLAIN, 25);
			Graphics2D backG = bi.createGraphics();
			backG.setColor(Color.WHITE); // 设置背景颜色
			backG.fillRect(0, 0, 200, 50);
			backG.setFont(titleFont);
			backG.setColor(Color.BLUE);// 设置字体颜色
			backG.drawString(content, 10, 30);
			backG.dispose();
			baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "JPEG", baos);;
			byte[] bytes = baos.toByteArray();
			return Base64.getEncoder().encodeToString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(createImg("111"));
	}
}
