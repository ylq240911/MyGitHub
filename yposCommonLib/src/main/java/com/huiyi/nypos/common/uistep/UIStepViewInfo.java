package com.huiyi.nypos.common.uistep;

import android.content.Intent;

/**
 * step view info
 * @author adam
 *
 */
public class UIStepViewInfo {
	private Class<? extends BaseUIStepView> stepViewClass = null; //step view class
	private Intent intent = null;  //输入的intent
	private int resID = -1;  //view resorce id
	private String tag = null; //view tag
	private String title=null; //view title
	
	public Class<? extends BaseUIStepView> getStepViewClass() {
		return stepViewClass;
	}

	public void setStepViewClass(Class<? extends BaseUIStepView> stepViewClass) {
		this.stepViewClass = stepViewClass;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public int getResID() {
		return resID;
	}

	public void setResID(int resID) {
		this.resID = resID;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	public UIStepViewInfo(String tag,String title, BaseUIStepView fragment) {
		this.tag = tag;
		this.title=title;
		this.stepViewClass = fragment.getClass();
	}

	public UIStepViewInfo(String tag,String title,Class<? extends BaseUIStepView> stepViewClass,
			int resID) {
		this.tag = tag;
		this.title=title;
		this.stepViewClass = stepViewClass;
		this.resID = resID;
	}
	

	public UIStepViewInfo(String tag,String title,Class<? extends BaseUIStepView> stepViewClass,
			Intent intent,int resID) {
		this.tag = tag;
		this.title=title;
		this.stepViewClass = stepViewClass;
		this.intent = intent;
		this.resID = resID;
	}

	public UIStepViewInfo(String tag,String title,Class<? extends BaseUIStepView> stepViewClass) {
		this.tag = tag;
		this.title=title;
		this.stepViewClass = stepViewClass;
	}

	public UIStepViewInfo(String tag,String title,Class<? extends BaseUIStepView> stepViewClass,
			Intent intent) {
		this.tag = tag;
		this.title=title;
		this.stepViewClass = stepViewClass;
		this.intent = intent;
	}
	
	

	/**
	 * 创建新的step view 实例
	 * @return
	 */
	public BaseUIStepView getNewStepView() {
		try {
			BaseUIStepView fragment = this.stepViewClass
					.newInstance();
			if (this.resID != -1)
				fragment.setContentView(this.resID);
			if (this.intent != null)
				fragment.setIntent(this.intent);
			if (this.tag != null) 
				fragment.setStepTag(this.tag);
			if (this.tag != null) 
				fragment.setStepTitle(this.title);			
			return fragment;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
