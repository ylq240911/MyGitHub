package com.huiyi.nypos.common.uistep;

import android.content.Intent;

/**
 * step 接口类
 * @author adam
 *
 */
public interface IUIStep {
	/**
	 * 获取step指定tag view
	 * @param tag
	 * @return
	 */
	BaseUIStepView getStepView(String tag);

	IUIStep addStepView(String tag, String title,
						Class<? extends BaseUIStepView> stepFragmentClass);

	IUIStep addStepView(String tag, String title,
						Class<? extends BaseUIStepView> stepFragmentClass, int resID);

	IUIStep addStepView(String tag, String title,
						Class<? extends BaseUIStepView> stepFragmentClass, Intent intent);

	IUIStep addStepView(String tag, String title,
						Class<? extends BaseUIStepView> stepFragmentClass, int resID,
						Intent intent);

	IUIStep mergeStepView(IUIStep paramUIStep);

	int getPagesCount();
}
