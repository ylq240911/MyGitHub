package com.huiyi.nypos.common.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HySSLContext {
	public static final String CLIENT_AGREEMENT = "TLS";// 使用协议
	public static final String CLIENT_KEY_MANAGER = "X509";// 密钥管理器
	public static final String CLIENT_TRUST_MANAGER = "X509";// 信任证书管理器
	public static final String CLIENT_KEY_KEYSTORE = "pkcs12";// 密库，这里用的是BouncyCastle密库
	public static final String PASSWORD = "k$s@ore";															// pkcs12 BKS
	
	private static final String TAG = "SSLContext";

	/** 
     * 获取Https的证书 
     * @param context Activity（fragment）的上下文 
     * @return SSL的上下文对象 
     */  
    public static SSLContext getSSLContext(byte[] buf) {  
          
    	SSLContext context = null;
		// KeyStore tks = null;//信任证书,公钥

		try {
			context = SSLContext.getInstance(CLIENT_AGREEMENT);
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance(CLIENT_TRUST_MANAGER);
			KeyManagerFactory keyManager = null;
			keyManager = KeyManagerFactory.getInstance(CLIENT_KEY_MANAGER);
			String type = KeyStore.getDefaultType();
			// 信任证书,公钥
			KeyStore tks = KeyStore.getInstance(CLIENT_KEY_KEYSTORE);
			System.setProperty ("jsse.enableSNIExtension", "false");
			/*
			 * InputStream caStream = appContext.getApplicationContext()
			 * .getAssets().open("ca.crt");
			 */
			InputStream caStream = new ByteArrayInputStream(buf);
			// 初始化信任证书管理器
			tks.load(caStream, PASSWORD.toCharArray());

			keyManager.init(tks, PASSWORD.toCharArray());
			tmf.init(tks);

			context.init(keyManager.getKeyManagers(), tmf.getTrustManagers(),
					null);
			return context;
		} catch (Exception e) {
			e.printStackTrace();
			PosLogger.e("DefHttpHelper", e.getMessage(), e);
			return null;
		}
    } 
}
