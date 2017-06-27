package com.huiyi.nypos.common.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * activity收集器，统一管理运行中的activity
 * @author adam
 *
 */
public class ActivityCollector {

	private static List<Activity> activities = new ArrayList<Activity>();
	
	private ActivityCollector() {}
	
	/**
	 * 注册新的Activity
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		activities.add(activity);
	}
	
	/**
	 * 注销已销毁的Activity
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		if (!activities.isEmpty()) {
			activities.remove(activity);
		}
	}
	
	/**
	 * 关闭应用所有的Activity
	 */
	public static void finishAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();				
			}
		}
		activities.clear();
	}
	
	/**
	 * 获取最上层的Activity，即当前顶层可见的Activity。
	 * @return
	 */
	public static Activity getTopActivity() {
		if (activities.isEmpty()) {
			return null;
		}
		
		int index = activities.size() - 1;
		return activities.get(index);	
	}
	
	public static int getActivitySize() {
		if (activities.isEmpty()) {
			return 0;
		}
		return activities.size();
	}
}
