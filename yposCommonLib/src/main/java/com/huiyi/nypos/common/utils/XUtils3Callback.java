package com.huiyi.nypos.common.utils;

public interface XUtils3Callback {

	void onSuccess(String result);

	/**
	 * 请求失败、拦截到错误等，回调的方法
	 * 
	 * @param mTResult
	 *            错误信息 根据自己的项目接口的返回去model
	 * @param message
	 *            提示信息
	 */
	void onError(String code, String message);

	/**
	 * 请求结束回调的方法
	 */
	void onFinished();

}
