package com.huiyi.nypos.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;



import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class HttpClientHelper {
	public static int time_out = 60000;
	public static void setTimeOut(int timeout) {
		time_out = timeout * 1000;
	}
	/**
     * Post请求连接Https服务
     * @param serverURL  请求地址
     * @param jsonStr    请求报文
     * @return
     * @throws Exception
     */
    public static synchronized String doHttpsPost(String serverURL, String jsonStr,byte[] buf)throws Exception {
        // 参数
        HttpParams httpParameters = new BasicHttpParams();
        // 设置连接超时
        HttpConnectionParams.setConnectionTimeout(httpParameters, time_out);
        // 设置socket超时
        HttpConnectionParams.setSoTimeout(httpParameters, time_out);
        // 获取HttpClient对象 （认证）
        HttpClient hc = initHttpClient(httpParameters,buf);
     //   HttpClient hc = requestHTTPSPage(buf);
        HttpPost post = new HttpPost(serverURL);
        // 发送数据类型
        post.addHeader("Content-Type", "application/json;charset=utf-8");
        // 接受数据类型
        post.addHeader("Accept", "application/json");
        // 请求报文
        StringEntity entity = new StringEntity(jsonStr, "UTF-8");
        post.setEntity(entity);
        post.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(post);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }

    private static HttpClient client = null;
    /**
     * 初始化HttpClient对象
     * @param params
     * @return
     */
    public static synchronized HttpClient initHttpClient(HttpParams params,byte[] buf) {
        if(client == null){
        	InputStream caStream = new ByteArrayInputStream(buf);
            try {
                KeyStore trustStore = KeyStore.getInstance(CLIENT_KEY_KEYSTORE);
                trustStore.load(caStream, PASSWORD.toCharArray());
            //    trustStore.load(null, null);
                SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                //允许所有主机的验证
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
                // 设置http和https支持
                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));
                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
                caStream.close();
                return new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
            	 try {
					caStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                e.printStackTrace();
                return new DefaultHttpClient(params);
            }
        }
        return client;
    }
   
	public static final String CLIENT_KEY_MANAGER = "X509";// 密钥管理器
	public static final String CLIENT_TRUST_MANAGER = "X.509";// 信任证书管理器
	public static final String CLIENT_KEY_KEYSTORE = "pkcs12";// 密库，这里用的是BouncyCastle密库
																// pkcs12 BKS
	public static final String PASSWORD = "k$s@ore";
    public static HttpClient requestHTTPSPage(byte[] buf) {
        //InputStream ins = null;
    	  HttpClient mHttpClient = new DefaultHttpClient();
    	InputStream caStream = new ByteArrayInputStream(buf);
        String result = "";
        try {
        	
            KeyStore keyStore = KeyStore.getInstance(CLIENT_KEY_KEYSTORE);
            keyStore.load(caStream, PASSWORD.toCharArray());
            CertificateFactory cerFactory = CertificateFactory.getInstance(CLIENT_KEY_MANAGER);
            Certificate cer = cerFactory.generateCertificate(caStream);
            keyStore.setCertificateEntry("trust", cer);
            SSLSocketFactory sf = new SSLSocketFactoryEx(keyStore);
            Scheme sch = new Scheme("https", sf, 443);
            mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);
          
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (caStream != null)
                	caStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mHttpClient;
    }
   
}
