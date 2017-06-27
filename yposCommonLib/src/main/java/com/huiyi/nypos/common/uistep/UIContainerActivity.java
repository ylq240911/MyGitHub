package com.huiyi.nypos.common.uistep;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

/**
 * step 容器 activity类
 * @author adam
 *
 */
public abstract class UIContainerActivity extends Activity {
	private int fragmentContainerViewId;  // fragement view 依附的容器id 
	private Handler mainHandler = new Handler();
	private FragmentManager fragmentManager;
	private UIStepManager stepManager = new UIStepManager();
//	private boolean isEmulator = false;
	public static final String ACTIVITY_LAYOUT_RESOURCE = "activity_layot_resource_id";
	public static final String FRAGMENT_RESOURCE = "fragment_id";
	public static final String FIRST_FRAGMENT = "first_fragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		int layout = intent.getIntExtra(ACTIVITY_LAYOUT_RESOURCE, 0);
		int viewID = intent.getIntExtra(FRAGMENT_RESOURCE, 0);
		String fragmentIndexName = intent.getStringExtra(FIRST_FRAGMENT);
		if (viewID != 0) {
			this.fragmentContainerViewId = viewID;
		}
		if (layout != 0) {
			setContentView(layout);
		}
		this.fragmentManager = getFragmentManager();
		onInitSteps();
		loadStepView(fragmentIndexName);
	}

	/**
	 * 设置fragment Container view id, stepview 将依附于此
	 * @param id
	 */
	public void setFragmentContainerViewId(int id) {
		this.fragmentContainerViewId = id;
	}

	@Override
	public void onAttachedToWindow() {		
		super.onAttachedToWindow();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		boolean consumedThisKeyEvent = false;
		if ((this.stepManager.currentStepView() != null)
				&& (this.stepManager.currentStepView().isResumed())) {
			consumedThisKeyEvent = this.stepManager.currentStepView()
					.dispatchKeyEvent(event);
		}
		if (consumedThisKeyEvent) {
			return true;
		}
		if (event.getAction() == 1) {
			BaseUIStepView fragment = null;
			Fragment view = this.stepManager.currentStepView();
			if ((view != null) && ((view instanceof BaseUIStepView)))
				fragment = (BaseUIStepView) view;
			else {
				return super.dispatchKeyEvent(event);
			}

			int keyCode = event.getKeyCode();
			if (keyCode == 66) {
				if (fragment != null) {
					fragment.onKeyNext();
					return true;
				}
			} else if (keyCode == 4) {
				backStepView();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 加载显示当前step 指定tag的stepview
	 * @param tag
	 */
	private void loadStepView(String tag) {
		BaseUIStepView view = this.stepManager.initFragment(tag);
		if (view != null) {
			FragmentTransaction transaction = this.fragmentManager
					.beginTransaction();
			transaction.add(this.fragmentContainerViewId, view);
			transaction.commitAllowingStateLoss();
		}
	}

	/**
	 * add single step to activity
	 * @param step
	 */
	public void addStep(IUIStep step) {
		this.stepManager.addStep(step);
	}

	/**
	 * add multi step to activity
	 * @param steps
	 */
	public void addStep(IUIStep[] steps) {
		this.stepManager.addStep(steps);
	}

	/**
	 * 加载显示下一个step的指定tag view
	 * @param tag
	 */
	public void nextStepView(String tag) {
		if (this.stepManager.hasNext()) {
			BaseUIStepView nextStepView = this.stepManager.nextStep(tag);
			if (nextStepView != null) {
				FragmentTransaction transaction = this.fragmentManager
						.beginTransaction();
				transaction.replace(this.fragmentContainerViewId, nextStepView);
				transaction.commitAllowingStateLoss();
			}
		}
	}

	/**
	 * 加载显示当前step 指定tag的stepview
	 * @param tag
	 */
	public void nextSlibingStep(String tag) {
		BaseUIStepView nextStepView = this.stepManager.nextSlibing(tag);
		if (nextStepView != null) {
			FragmentTransaction transaction = this.fragmentManager
					.beginTransaction();
			transaction.replace(this.fragmentContainerViewId, nextStepView);
			transaction.commitAllowingStateLoss();
		}
	}

	/**
	 * 回退到上一个view
	 */
	public void backStepView() {
		if (this.stepManager.isFirstStep()) {
			finish();
		} else {
			this.stepManager.backStep();
			FragmentTransaction transaction = this.fragmentManager
					.beginTransaction();
			transaction.replace(this.fragmentContainerViewId, this.stepManager.currentStepView());
			transaction.commitAllowingStateLoss();
		}
	}

	/**
	 * 回退到第一个view
	 */
	public void back2FirstStepView() {
		do
			this.stepManager.backStep();
		while (!this.stepManager.isFirstStep());
		FragmentTransaction transaction = this.fragmentManager
				.beginTransaction();
		transaction.replace(this.fragmentContainerViewId, this.stepManager.currentStepView());
		transaction.commitAllowingStateLoss();
	}

	@Override
	public void finish() {
		super.finish();
	}

	/**
	 * 当activity回退时触发
	 */
	@Override
	public void onBackPressed() {
		if (this.stepManager.currentStepView() == null) {
			super.onBackPressed();
			return;
		}
		if (this.stepManager.isFirstStep()) {
			finish();
		} else {
			super.onBackPressed();
			this.stepManager.backStep();
		}
	}

	/**
	 * 获取主线程
	 * @return
	 */
	public Handler getMainHandler() {
		return this.mainHandler;
	}

	/**
	 * 将流程所有步骤step预加载到actvitiy中
	 */
	protected abstract void onInitSteps();
}