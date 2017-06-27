package com.huiyi.nypos.common.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.util.Base64;

/**
 * 加解密工具类
 * 
 * @author adam
 * 
 */
public class EncryptUtil {
	public static final String UTF8 = "UTF-8";

	/**
	 * HMAC加密 数据
	 * 
	 * @param data
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptHMAC(byte[] data, byte[] secret)
			throws Exception {
		byte[] bytes = null;
		SecretKey secretKey = new SecretKeySpec(secret, "HmacMD5");
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		bytes = mac.doFinal(data);
		return bytes;
	}

	/**
	 * HMAC加密 数据
	 * 
	 * @param data
	 *            utf-8
	 * @param secret
	 *            utf-8
	 * @return
	 * @throws Exception
	 */
	public static String encryptHMAC(String data, String secret)
			throws Exception {

		return encryptBASE64(encryptHMAC(data.getBytes(UTF8),
				secret.getBytes(UTF8)));
	}

	/**
	 * MD5 加密数据
	 * 
	 * @param data
	 *            utf-8
	 * @return
	 * @throws IOException
	 */
	public static byte[] encryptMD5(String data) throws Exception {

		return encryptMD5(data.getBytes(UTF8));

	}

	/**
	 * MD5 加密数据
	 * 
	 * @param data
	 *            byte[]
	 * @return
	 * @throws IOException
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {
		byte[] bytes = null;

		MessageDigest md = MessageDigest.getInstance("MD5");
		bytes = md.digest(data);

		return bytes;
	}

	/**
	 * 3DES加密
	 * 
	 * @param data
	 *            utf-8
	 * @param secret
	 *            大于16个ascii字符
	 * @return
	 * @throws Exception
	 */
	public static String encrypt3Des(String data, String secret)
			throws Exception {
		return encryptBASE64(encrypt3Des(data.getBytes(UTF8), secret.getBytes()));
	}

	/**
	 * 3DES解密
	 * 
	 * @param data
	 *            base64 utf-8
	 * @param secret
	 *            大于16个ascii字符
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String decrypt3Des(String data, String secret)
			throws Exception {
		return new String(decrypt3Des(decryptBASE64(data), secret.getBytes()));
	}

	/**
	 * 3DES加密
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("TrulyRandom")
	public static byte[] encrypt3Des(byte[] data, byte[] secret)
			throws Exception {
		Cipher cipher = Cipher.getInstance("DESEDE/CBC/PKCS5Padding");
		DESedeKeySpec desKeySpec = new DESedeKeySpec(secret);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] ret = cipher.doFinal(data);

		return ret;
	}

	/**
	 * 3DES解密
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt3Des(byte[] data, byte[] secret)
			throws Exception {
		Cipher cipher = Cipher.getInstance("DESEDE/CBC/PKCS5Padding");
		DESedeKeySpec desKeySpec = new DESedeKeySpec(secret);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] ret = cipher.doFinal(data);

		return ret;
	}

	/**
	 * DES加密
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptDes(byte[] data, byte[] secret, byte[] iv)
			throws Exception {
		// 实例化IvParameterSpec对象，使用指定的初始化向量
		// 实例化SecretKeySpec类，根据字节数组来构造SecretKey
		SecretKeySpec key = new SecretKeySpec(secret, "DES");
		// 创建密码器
		Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
		// 用秘钥初始化Cipher对象
		IvParameterSpec ivparam = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivparam);
		// 执行加密操作
		byte[] encryptedData = cipher.doFinal(data);

		return encryptedData;

	}

	/**
	 * DES解密
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptDes(byte[] data, byte[] secret, byte[] iv)
			throws Exception {
		// 实例化IvParameterSpec对象，使用指定的初始化向量
		// 实例化SecretKeySpec类，根据字节数组来构造SecretKey
		SecretKeySpec key = new SecretKeySpec(secret, "DES");
		// 创建密码器
		Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
		// 用秘钥初始化Cipher对象
		IvParameterSpec ivparam = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, key, ivparam);
		// 执行解密操作
		return cipher.doFinal(data);
	}

	/**
	 * 2DES加密 cbc模式
	 * 
	 * @param data
	 *            utf-8
	 * @param secret
	 *            大于16个ascii字符
	 * @return
	 * @throws Exception
	 */
	public static String encrypt2CBCDes(String data, String secret)
			throws Exception {

		byte[] iv = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

		byte[] byts = data.getBytes(UTF8);
		if (byts.length % 8 != 0) { // 补足8的倍数
			byts = Arrays.copyOf(byts, byts.length + (8 - byts.length % 8));
		}

		return encryptBASE64(encrypt2CBCDes(byts, hex2byte(secret), iv));
	}

	/**
	 * 2DES解密 cbc模式
	 * 
	 * @param data
	 *            base64 utf-8
	 * @param secret
	 *            大于16个ascii字符
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String decrypt2CBCDes(String data, String secret)
			throws Exception {

		byte[] iv = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

		return new String(
				decrypt2CBCDes(decryptBASE64(data), hex2byte(secret), iv));
	}

	/**
	 * 2des加密 cbc模式
	 * @param data
	 * @param secret
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt2CBCDes(byte[] data, byte[] secret, byte[] iv)
			throws Exception {
		List<byte[]> encryptDataList = new ArrayList<byte[]>();
		int len = 0;
		for (int i = 0; i <= data.length - 8; i = i + 8) {
			byte[] byts = encrypt2Des(Arrays.copyOfRange(data, i, i + 8),
					secret, iv);
			encryptDataList.add(byts);
			len =len+ byts.length;
		}
		byte[] encryptData = new byte[len];
		int j = 0;
		for (int i = 0; i < encryptDataList.size(); i++) {
			System.arraycopy(encryptDataList.get(i), 0, encryptData, j,
					encryptDataList.get(i).length);
			j =j+ encryptDataList.get(i).length;
		}
		return encryptData;
	}

	/**
	 * 2Des加密 双倍长DES加密中，密钥有32个字节。 步骤如下： 1、使用密钥的前16字节，对数据DATA进行加密，得到加密的结果TMP1；
	 * 2、使用密钥的后16字节，对第一的计算结果TMP1，进行解密，得到解密的结果TMP2；
	 * 3、再次使用密钥的前16字节，对第二次的计算结果TMP2，进行加密，得到加密的结果DEST。DEST就为最终的结果。
	 * 
	 * @param data
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt2Des(byte[] data, byte[] secret, byte[] iv)
			throws Exception {
		// 使用密钥的前16字节，对数据DATA进行加密，得到加密的结果TMP1;
		byte[] temp1 = encryptDes(data, Arrays.copyOf(secret, 8), iv);
		// 使用密钥的后16字节，对第一的计算结果TMP1，进行解密，得到解密的结果TMP2
		byte[] temp2 = decryptDes(temp1, Arrays.copyOfRange(secret, 8, 16), iv);
		// 再次使用密钥的前16字节，对第二次的计算结果TMP2，进行加密，得到加密的结果DEST。DEST就为最终的结果
		byte[] dest = encryptDes(temp2, Arrays.copyOf(secret, 8), iv);
		return dest;
	}

	/**
	 * 2des cbc解密
	 * @param data
	 * @param secret
	 * @param iv
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt2CBCDes(byte[] data, byte[] secret, byte[] iv)
			throws Exception {

		List<byte[]> encryptDataList = new ArrayList<byte[]>();
		int len = 0;
		for (int i = 0; i <= data.length - 8; i = i + 8) {
			byte[] byts = decrypt2Des(Arrays.copyOfRange(data, i, i + 8),
					secret, iv);
			encryptDataList.add(byts);
			len += byts.length;
		}
		byte[] encryptData = new byte[len];
		int j = 0;
		for (int i = 0; i < encryptDataList.size(); i++) {
			System.arraycopy(encryptDataList.get(i), 0, encryptData, j,
					encryptDataList.get(i).length);
			j += encryptDataList.get(i).length;
		}
		return encryptData;
	}

	/**
	 * 2Des解密 双倍长DES加密中，密钥有32个字节。 步骤与加密相反
	 * 
	 * @param data
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt2Des(byte[] data, byte[] secret, byte[] iv)
			throws Exception {
		// 使用密钥的前16字节解密
		byte[] temp1 = decryptDes(data, Arrays.copyOf(secret, 8), iv);
		// 使用密钥的后16字节，对数据temp1进行加密，得到加密的结果temp2;
		byte[] temp2 = encryptDes(temp1, Arrays.copyOfRange(secret, 8, 16), iv);
		// 再次使用密钥的前16字节，对第二次的计算结果TMP2，进行加密，得到加密的结果DEST。DEST就为最终的结果
		byte[] dest = decryptDes(temp2, Arrays.copyOf(secret, 8), iv);
		return dest;
	}

	/**
	 * base64加密
	 * 
	 * @param data
	 * @return
	 */
	public static String encryptBASE64(byte[] data) {
		return Base64.encodeToString(data, Base64.NO_WRAP);
	}

	/**
	 * base64解密
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decryptBASE64(String data) {
		return Base64.decode(data, Base64.NO_WRAP);
	}

	/**
	 * 将字节数组转换成16进制格式的字符串
	 * 
	 * @param bytData
	 *            字节数组
	 * @return
	 */
	public static String byte2hex(byte[] bytData) {
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < bytData.length; i++) {
			if ((bytData[i] & 0xff) < 0x10) {
				buffer.append("0");
			}
			buffer.append(Long.toString(bytData[i] & 0xff, 16));
		}

		return buffer.toString().toUpperCase(Locale.getDefault());
	}

	/**
	 * 将16进制格式的字符串转换成字节数组
	 * 
	 * @param hexs
	 *            16进制格式的字符串
	 * @return
	 */
	private static byte[] hex2byte(String hexs) {
		byte[] bytData = new byte[hexs.trim().length() / 2];
		hexs = hexs.trim();
		for (int i = 0; i < hexs.length(); i = i + 2) {
			bytData[i / 2] = (byte) Integer.parseInt(hexs.substring(i, i + 2),
					16);
		}

		return bytData;
	}

	public static void main(String[] args[]) {
		try {
			String str = encryptHMAC("1234567890", "123");
			System.out.print(str);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
