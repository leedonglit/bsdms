package com.isec.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;


public class RandomUtil {
	
	/**
	 * 位数不固定 根据哈希生成
	 * @return
	 */
	public static int hashRandom() {
		return Math.abs(UUID.randomUUID().toString().hashCode());
	}
	
	/**
	 * 位数固定  时间+随机数
	 * @return
	 */
	public static int timeAddRandom() {
		Random random = new Random();
		return Math.abs((int)(System.currentTimeMillis()+random.nextInt()));
	}
	
	public static void main(String[] args) {
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < 1000; i++) {
			int ii = hashRandom();
			System.out.println(ii);
			set.add(ii);
		}
		System.out.println(set.size());
	}
	
	
}
