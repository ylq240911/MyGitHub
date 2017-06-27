package com.huiyi.nypos.common.uistep;

/**
 * step 工厂类
 * @author adam
 *
 */
public class UIStepFactory {
	public static final int STEP_SINGLE = 0;
	public static final int STEP_MULTI = 1;

	public static IUIStep createStep(int type) {
		if (type == 0)
			return new UISingleStep();
		if (type == 1) {
			return new UIMultiStep();
		}
		return new UISingleStep();
	}

	public static IUIStep createSingleStep() {
		return new UISingleStep();
	}

	public static IUIStep createMultiStep() {
		return new UIMultiStep();
	}
}