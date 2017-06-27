package com.huiyi.nypos.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

public class DefHttpHelper {

	public static final String PASSWORD = "k$s@ore";
	private static final String METHOD_GET = "GET";
	private static final int HTTP_OK = 200; // 请求成功代码
	public static int time_out = 60000;

	public static final String CLIENT_AGREEMENT = "TLS";// 使用协议
	public static final String CLIENT_KEY_MANAGER = "X509";// 密钥管理器
	public static final String CLIENT_TRUST_MANAGER = "X509";// 信任证书管理器
	public static final String CLIENT_KEY_KEYSTORE = "pkcs12";// 密库，这里用的是BouncyCastle密库
																// pkcs12 BKS
	private static final String TAG = "DefHttpHelper";

	public static void setTimeOut(int timeout) {
		time_out = timeout * 1000;
	}

	/**
	 * Http Post 连接方式
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public static String httpPost(String url, String requestParams)
			throws Exception {
		Log.i("DefHttpHelper", "url:" + url + requestParams);
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {

			/*
			 * HttpsURLConnection .setDefaultHostnameVerifier(new
			 * HostnameVerifier() { public boolean verify(String string,
			 * SSLSession ssls) { return true; } });
			 */

			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setConnectTimeout(time_out);// 设置连接主机超时（单位：毫秒）
			PosLogger.i(TAG, "超时时间" + time_out);
			conn.setReadTimeout(time_out); // 设置从主机读取数据超时（单位：毫秒）
			// 防请求超时重发
			((HttpURLConnection) conn).setChunkedStreamingMode(0);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			if (requestParams != null && requestParams.length() > 0) {
				out.print(requestParams);
			}
			// flush输出流的缓冲
			out.flush();

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} finally {// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

	public static String httpsPost(Context appContext, String urlStr,
			String param) throws IOException {
		URL url = null;
		HttpsURLConnection conn = null;
		StringBuffer result = new StringBuffer();

		System.setProperty("javax.net.debug", "ssl,handshake");
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
			InputStream caStream = appContext.getApplicationContext()
					.getAssets().open("ca.crt");
			// 初始化信任证书管理器
			tks.load(caStream, PASSWORD.toCharArray());

			keyManager.init(tks, PASSWORD.toCharArray());
			tmf.init(tks);

			context.init(keyManager.getKeyManagers(), tmf.getTrustManagers(),
					null);
		} catch (Exception e) {
			e.printStackTrace();
			PosLogger.e("DefHttpHelper", e.getMessage(), e);
			return null;
		}

		SSLSocketFactory factory = context.getSocketFactory();

		PosLogger.d("DefHttpHelper", "url:" + urlStr + param + "超时时间："
				+ time_out);
		url = new URL(urlStr);
		conn = (HttpsURLConnection) url.openConnection();
		conn.setSSLSocketFactory(factory);
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		conn.setConnectTimeout(time_out);// 设置连接主机超时（单位：毫秒）
	
		conn.setReadTimeout(time_out); // 设置从主机读取数据超时（单位：毫秒）
		conn.setHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});

		conn.setDoOutput(true);
		conn.setDoInput(true);

		conn.setUseCaches(false);
		conn.setRequestMethod("GET");
		// 获取URLConnection对象对应的输出流
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		if (param != null && param.length() > 0) {
			out.print(param);
		}
		// flush输出流的缓冲
		out.flush();
		BufferedReader in = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} finally {// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if(conn!=null){
					conn.disconnect();
				}
				
			} catch (IOException ex) {
				PosLogger.e(TAG, ex.getMessage(), ex);
				ex.printStackTrace();
			}
		}
		return result.toString();
	}

	public static String httpsPost(String urlStr,
			String param, byte[] buf) throws IOException {
		URL url = null;
		HttpsURLConnection conn = null;
		StringBuffer result = new StringBuffer();

		System.setProperty("javax.net.debug", "ssl,handshake");
		
		SSLContext context = HySSLContext.getSSLContext(buf);
		SSLSocketFactory factory = context.getSocketFactory();

		PosLogger.d("DefHttpHelper", "url:" + urlStr + param + "超时时间："
				+ time_out);
		url = new URL(urlStr);
		conn = (HttpsURLConnection) url.openConnection();
		conn.setSSLSocketFactory(factory);

		conn.setConnectTimeout(time_out);// 设置连接主机超时（单位：毫秒）
		conn.setReadTimeout(time_out); // 设置从主机读取数据超时（单位：毫秒）
		conn.setHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}

		});

		conn.setDoOutput(true);
		conn.setDoInput(true);
		if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
			conn.setRequestProperty("Connection", "close");
		}
		
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		// 获取URLConnection对象对应的输出流
		PrintWriter out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		if (param != null && param.length() > 0) {
			out.print(param);
		}
		// flush输出流的缓冲
		out.flush();
		BufferedReader in = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应

			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "UTF-8");
			in = new BufferedReader(inputStreamReader);
			String line;
			if (in != null) {
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
			}

		} finally {// 使用finally块来关闭输出流、输入流
			//try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			//} catch (IOException ex) {
				//ex.printStackTrace();
			//}
		}
		return result.toString();
	}

	public static Bitmap getBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);

			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static void httpDownLoad(String urlStr, String savePath,
			String filename, DownLoadListener listener) {
		try {
			File file = new File(savePath, filename);
			if (!file.exists()) {
				if (!file.createNewFile()) {
					listener.onError("文件不存在，创建失败!");
				}
			}

			int len = 0;
			URL url = null;
			HttpURLConnection conn = null;
			InputStream inStream = null;
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(time_out);
			conn.setRequestMethod(METHOD_GET);
			conn.setRequestProperty("accept", "*/*");
			conn.connect();
			int responseCode = conn.getResponseCode();
			if (responseCode == HTTP_OK) {
				if (listener != null) {
					listener.onStart(conn.getContentLength());
				}
				inStream = conn.getInputStream();
				FileOutputStream outputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int readTotalSize = 0;
				while ((len = inStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, len);
					readTotalSize += len;
					if (listener != null) {
						listener.onProgress(readTotalSize);
					}
				}
				outputStream.flush();
				outputStream.close();
				inStream.close();

				if (listener != null) {
					listener.onComplete(file);
				}
			} else {
				if (listener != null) {
					listener.onError("请求失败:" + responseCode);
				}
			}
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			PosLogger.e(TAG, e.getMessage(), e);
			if (listener != null) {
				listener.onError("下载失败");
			}
		}
	}

	public interface DownLoadListener {
		void onStart(int fileSize);

		void onProgress(int currentSize);

		void onComplete(File file);

		void onError(String errInfo);
	}

}
