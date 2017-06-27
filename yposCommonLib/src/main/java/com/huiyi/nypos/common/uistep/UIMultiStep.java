package com.huiyi.nypos.common.uistep;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * 多step
 * @author adam
 *
 */
public class UIMultiStep implements IUIStep {
	private Map<String, UIStepViewInfo> stepViewInfos = new HashMap<String, UIStepViewInfo>();

	public BaseUIStepView getStepView(String tag) {
		UIStepViewInfo info = this.stepViewInfos.get(tag);
		if (info != null) {
			return info.getNewStepView();
		}
		return null;
	}

	public IUIStep addStep(String tag,String title, BaseUIStepView view) {
		this.stepViewInfos.put(tag, new UIStepViewInfo(tag,title, view));
		return this;
	}

	public IUIStep addStepView(String tag,String title,
			Class<? extends BaseUIStepView> stepViewClass) {
		UIStepViewInfo temp = new UIStepViewInfo(tag, title,stepViewClass);
		this.stepViewInfos.put(tag, temp);
		return this;
	}

	public IUIStep addStepView(String tag,String title,
			Class<? extends BaseUIStepView> stepViewClass, int resID) {
		UIStepViewInfo temp = new UIStepViewInfo(tag, title,stepViewClass, resID);
		this.stepViewInfos.put(tag, temp);
		return this;
	}

	public IUIStep addStepView(String tag,String title,
			Class<? extends BaseUIStepView> stepViewClass, Intent intent) {
		UIStepViewInfo temp = new UIStepViewInfo(tag,title, stepViewClass, intent);
		this.stepViewInfos.put(tag, temp);
		return this;
	}

	public IUIStep addStepView(String tag,String title,
			Class<? extends BaseUIStepView> stepViewClass, int resID, Intent intent) {
		UIStepViewInfo temp = new UIStepViewInfo(tag,title, stepViewClass,
				intent, resID);
		this.stepViewInfos.put(tag, temp);
		return this;
	}

	public IUIStep mergeStepView(IUIStep step) {
		if ((step instanceof UIMultiStep)) {
			UIMultiStep temp = (UIMultiStep) step;
			this.stepViewInfos.putAll(temp.stepViewInfos);
		}
		return this;
	}

	public int getPagesCount() {
		return this.stepViewInfos.size();
	}

	
}