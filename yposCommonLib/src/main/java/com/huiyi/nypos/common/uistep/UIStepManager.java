package com.huiyi.nypos.common.uistep;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * step 管理器
 * @author adam
 *
 */
public class UIStepManager {
	
	private int index = 0; //当前step index索引，对应steps列表
	private List<IUIStep> steps = null;  //step 管理列表
	private Stack<BaseUIStepView> viewStack = null; //step view 已显示的堆栈

	public UIStepManager() {
		this.steps = new ArrayList<IUIStep>();
		this.viewStack = new Stack<BaseUIStepView>();
	}

	/**
	 * 增加一个step
	 * @param step
	 */
	public void addStep(IUIStep step) {
		this.steps.add(step);
	}


	/**
	 * 增加一批step
	 * @param steps
	 */
	public void addStep(IUIStep[] steps) {
		if (steps == null) {
			return;
		}
		for (IUIStep step : steps)
			this.steps.add(step);
	}

	/**
	 * 是否还有step未显示
	 * @return
	 */
	public boolean hasNext() {
		return this.index + 1 < this.steps.size();
	}

	//创建指定tag的step view
	public BaseUIStepView initFragment(String tag) {
		BaseUIStepView stepFragment = nextSlibing(tag);
		if (stepFragment != null) {
			this.viewStack.push(stepFragment);
		}
		return stepFragment;
	}

	/**
	 * 创建同一个step中，指定tag的view
	 * @param tag
	 * @return
	 */
	public BaseUIStepView nextSlibing(String tag) {
		IUIStep step = null;
		BaseUIStepView fragment = null;
		if ((this.steps != null) && (this.steps.size() > 0)) {
			step = this.steps.get(this.index);
			fragment = step.getStepView(tag);
		}
		return fragment;
	}

	/**
	 * 创建下一个step中，指定tag的view
	 * @param tag
	 * @return
	 */
	public BaseUIStepView nextStep(String tag) {
		if (this.index + 1 >= this.steps.size()) {
			return null;
		}
		IUIStep stepView = this.steps.get(this.index + 1);
		if (stepView == null) {
			return null;
		}
		BaseUIStepView stepFragment = stepView.getStepView(tag);
		if (stepFragment != null) {
			this.viewStack.push(stepFragment);
			this.index += 1;
		}
		return stepFragment;
	}

	/**
	 * 当前的step view
	 * @return
	 */
	public BaseUIStepView currentStepView() {
		if (this.viewStack.isEmpty()) {
			return null;
		}
		return this.viewStack.peek();
	}

	/**
	 * 回退到上一个step view
	 */
	public void backStep() {
		if (this.index > 0) {
			this.index -= 1;
			this.viewStack.pop();
		}
	}

	/**
	 * 当前是否为第一个step view
	 * @return
	 */
	public boolean isFirstStep() {
		return this.viewStack.size() == 1;
	}
}