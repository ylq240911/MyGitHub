package com.huiyi.nypos.common.uistep;

import android.content.Intent;

/**
 * 单step
 * @author adam
 *
 */
public class UISingleStep implements IUIStep {
	
	UIStepViewInfo stepViewInfo =null;
	
	public BaseUIStepView getStepView(String tag) {
		return stepViewInfo.getNewStepView();
	}

	public IUIStep addStepView(String tag,String title,
			Class<? extends BaseUIStepView> stepViewClass) {
		stepViewInfo = new UIStepViewInfo(tag,title,stepViewClass);
		return this;
	}

	public IUIStep addStepView(String tag,String title,
			Class<? extends BaseUIStepView> stepViewClass, int resID) {
		stepViewInfo = new UIStepViewInfo(tag,title,stepViewClass,resID);
		return this;
	}

	public IUIStep addStepView(String tag,String title,
			Class<? extends BaseUIStepView> stepViewClass, Intent intent) {
		stepViewInfo = new UIStepViewInfo(tag,title,stepViewClass,intent);
		return this;
	}

	public IUIStep addStepView(String tag,String title,
			Class<? extends BaseUIStepView> stepViewClass, int resID, Intent intent) {
		stepViewInfo = new UIStepViewInfo(tag,title,stepViewClass,intent,resID);
		return this;
	}

	public IUIStep mergeStepView(IUIStep step) {
		return null;
	}

	public int getPagesCount() {
		return 1;
	}
}
