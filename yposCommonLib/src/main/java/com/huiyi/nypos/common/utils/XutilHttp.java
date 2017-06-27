package com.huiyi.nypos.common.utils;

import javax.net.ssl.SSLContext;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;



public class XutilHttp {
	protected static final String TAG = "XutilHttp";

	/**
	 * 发送请求 Cancelable
	 * 
	 * @param method
	 *            请求方式(GET POST)
	 * @param params
	 *            请求参数
	 * @param mCallback
	 *            回调对象
	 * @return 网络请求的Cancelable 可以中断请求
	 */
	public static Cancelable sendHttpRequest1(HttpMethod method,
			RequestParams params, int timeOut, byte[] buf,
			final XUtils3Callback mCallback) {
		int time_out = 60;
		if (timeOut > 0) {
			time_out = timeOut;
		}
		if (params == null) {
			params = new RequestParams();
		}
		params.setCacheMaxAge(0 * 1000); // 为请求添加缓存时间
		params.setConnectTimeout(time_out * 1000); // 超时时间60s

		SSLContext sslContext = HySSLContext.getSSLContext(buf);
		if (null == sslContext) {
			return null;
		}
		params.setSslSocketFactory(sslContext.getSocketFactory()); // 绑定SSL证书(https请求)

		Cancelable cancelable = x.http().request(method, params,
				new Callback.CacheCallback<String>() {
					@Override
					// 取消
					public void onCancelled(CancelledException msg) {
						mCallback.onError("203", msg.getMessage());
						mCallback.onFinished();
					}

					@Override
					// 错误
					public void onError(Throwable arg0, boolean arg1) {

						PosLogger.e(TAG, arg0.getMessage(), arg0);
						mCallback.onError("303", "网络不稳定，请稍后再试");
						mCallback.onFinished();
					}

					@Override
					// 成功
					public void onSuccess(String result) {
						try {
							PosLogger.e(TAG, "onSuccess");
							if (result != null) {
								mCallback.onSuccess(result);
							} else {
								PosLogger.e(TAG, "result为null");
							}

						} catch (Exception e) {
							PosLogger.e(TAG, e.getMessage(), e);
							mCallback.onError("225", "终端处理失败:" + e.getMessage());
							mCallback.onFinished();
						}

					}

					@Override
					// 完成
					public void onFinished() {
					}

					@Override
					public boolean onCache(String arg0) {
						// 在setCacheMaxAge设置范围（上面设置的是60秒）内，如果再次调用GET请求，
						// 返回true：缓存内容被返回，相信本地缓存，返回false：缓存内容被返回，不相信本地缓存，仍然会请求网络
						PosLogger.i("onCache", "cache：" + arg0);
						if (arg0 != null) {
							mCallback.onSuccess(arg0);
						}
						return true;
					}
				});
		return cancelable;
	}

	public static Cancelable sendHttpRequest(HttpMethod method,
			RequestParams params, int timeOut, byte[] buf,
			final XUtils3Callback mCallback) {
		int time_out = 60;
		if (timeOut > 0) {
			time_out = timeOut;
		}
		if (params == null) {
			params = new RequestParams();
		}
		params.setCacheMaxAge(0 * 1000); // 为请求添加缓存时间
		params.setConnectTimeout(time_out * 1000); // 超时时间60s

		SSLContext sslContext = HySSLContext.getSSLContext(buf);
		if (null == sslContext) {
			return null;
		}
		params.setSslSocketFactory(sslContext.getSocketFactory()); // 绑定SSL证书(https请求)

		Cancelable cancelable = x.http().request(method, params,
				new CommonCallback<String>() {
					@Override
					// 取消
					public void onCancelled(CancelledException msg) {
						mCallback.onError("201", msg.getMessage());
						mCallback.onFinished();
					}

					@Override
					// 错误
					public void onError(Throwable arg0, boolean arg1) {
						PosLogger.e(TAG, arg0.getMessage(), arg0);
						mCallback.onError("303", "网络不稳定，请稍后再试");
						mCallback.onFinished();
					}

					@Override
					// 成功
					public void onSuccess(String result) {
						try {
							PosLogger.e(TAG, "onSuccess");
							mCallback.onSuccess(result);
							mCallback.onFinished();
						} catch (Exception e) {
							PosLogger.e(TAG, e.getMessage(), e);
							mCallback.onError("225", "终端处理失败:" + e.getMessage());
							mCallback.onFinished();
						}
					}

					@Override
					// 完成
					public void onFinished() {
					}

				});
		return cancelable;
	}

	/**
	 * 中断网络请求
	 * 
	 * @param mCancelable
	 *            Cancelable
	 */
	public static void interrupt(Cancelable mCancelable) {
		if (mCancelable != null && !mCancelable.isCancelled()) {
			PosLogger.i(TAG, "网络中断");
			mCancelable.cancel();
		}
	}

}
