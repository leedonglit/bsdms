package com.isec.util;



/**
 *经纬度操作工具类
 * @author Administrator
 *
 */
public class LLAUtil {


	//根据经纬度获取目标旋转角度
	public static double getAngle(double lat_a,double lng_a,double lat_b,double lng_b) {
		double y= Math.sin(lng_b-lng_a)*Math.cos(lat_b);
		double x = Math.cos(lat_a)*Math.sin(lat_b)-Math.sin(lat_a)*Math.cos(lat_b)*Math.cos(lng_b-lng_a);
		double brng = Math.atan2(y,x);
		brng = Math.toDegrees(brng);
		if (brng<0) {
			brng = brng+360;
		}
		return brng;
	}
}
