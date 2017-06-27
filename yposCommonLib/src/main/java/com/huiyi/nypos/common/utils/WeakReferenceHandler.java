package com.huiyi.nypos.common.utils;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

/**
 * @author fanbaolong
 * @data  2016年9月5日 
 * @description  采用弱引用的形式去创建handler, 防止activity或是fragment关闭时导致内存泄漏
 */
public abstract class WeakReferenceHandler<T> extends Handler {
	private WeakReference<T> mReference;
	public WeakReferenceHandler(T reference) {
		mReference = new WeakReference<T>(reference);
	}
	@Override
	public void handleMessage(Message msg) {
		if (mReference.get() == null) {
			return;
		}
		handleMessage(mReference.get(), msg);
	}
	protected abstract void handleMessage(T reference, Message msg);
}
