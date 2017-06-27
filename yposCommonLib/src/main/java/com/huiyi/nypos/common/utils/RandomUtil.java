package com.huiyi.nypos.common.utils;

import java.util.Random;

/**
 * 随机数工具类
 * @author adam
 *
 */
public class RandomUtil {

	/**
	 * 0-9随机数字 
	 * @param length
	 * @return
	 */
	public static String getNumberRandom(int length){
		String base = "0123456789";
        return getRandom(base,length);
	}
	
	/**
	 * 0-9 a-z随机数字和字母 
	 * @param length
	 * @return
	 */
	public static String getNumberAndCharRandom(int length){
		String base = "0123456789abcdefghijklmnopqrstuvwxyz";
		return getRandom(base,length); 
	}
	
	/**
	 * 随机数
	 * @param base 字符基数范围
	 * @param length 
	 * @return
	 */
	public static String getRandom(String base,int length){
		Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
	}
	
}
