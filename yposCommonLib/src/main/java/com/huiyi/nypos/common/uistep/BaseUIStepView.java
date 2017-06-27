package com.huiyi.nypos.common.uistep;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Step view基类 ，继承于fragment
 * @author adam
 *
 */
public abstract class BaseUIStepView extends Fragment {
	private UIContainerActivity uiContainer; //依附的activity
	private int viewResourceId = -1;  //view 的 resource id
	private View view;  //view对象
	protected Intent curFragmentIntent = null;
	private String stepTag = null;  //step view tag
	private String stepTitle=null; // step view title
	
	public BaseUIStepView() {
	}

	/**
	 * 
	 * @param resourceId   fragment resource id
	 */
	public BaseUIStepView(int resourceId) {
		this.viewResourceId = resourceId;
	}

	/**
	 * 设置当前view的intent
	 * @param intent
	 */
	public void setIntent(Intent intent) {
		this.curFragmentIntent = intent;
		if (this.curFragmentIntent != null)
			onSetIntent();
	}

	protected void onSetIntent() {
		
	}

	/**
	 * 绑定actvity触发
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.uiContainer = ((UIContainerActivity) activity);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		onCreateFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (this.viewResourceId == -1) {
			return null;
		}
		this.view = inflater.inflate(this.viewResourceId, container, false);
		return this.view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		onInitViewPartment();
	}

	/**
	 * 设置view resource Id
	 * @param id
	 */
	protected void setContentView(int id) {
		this.viewResourceId = id;
	}

	/**
	 * 查找指定id的view
	 * @param id
	 * @return
	 */
	public View findViewById(int id) {
		if (this.view != null) {
			return this.view.findViewById(id);
		}
		return null;
	}

	/**
	 * 获取当前activity
	 * @return
	 */
	public final UIContainerActivity getStepActivity() {
		if (this.uiContainer == null) {
			this.uiContainer = ((UIContainerActivity) getActivity());
		}
		return this.uiContainer;
	}

	/**
	 * 获取application context
	 * @return
	 */
	public Context getApplicationContext(){
		return this.getActivity().getApplicationContext();
	}
	/**
	 * 显示下一个step指定的tag
	 * @param stepTag
	 */
	protected void nextStep(String stepTag) {
		if (this.uiContainer != null)
			this.uiContainer.nextStepView(stepTag);
	}

	/**
	 * 显示当前step指定的tag
	 * @param stepTag
	 */
	protected void nextSlibingStep(String stepTag) {
		if (this.uiContainer != null)
			this.uiContainer.nextSlibingStep(stepTag);
	}

	/**
	 * 回退到上一个view
	 */
	protected void backStep() {
		if (this.uiContainer != null)
			this.uiContainer.backStepView();
	}

	/**
	 * 回退到第一个view
	 */
	protected void back2FirstStep() {
		if (this.uiContainer != null)
			this.uiContainer.back2FirstStepView();
	}

	/**
	 * 结束所有的step
	 */
	protected void finishStep() {
		if (this.uiContainer != null)
			this.uiContainer.finish();
	}

	/**
	 * 运行于主线程任务
	 * @param action
	 */
	protected void runOnUiThread(Runnable action) {
		if (this.uiContainer != null)
			this.uiContainer.runOnUiThread(action);
	}

	protected void onKeyNext() {
	}

	/**
	 * 创建fragment时触发，一般用于给view赋viewid
	 */
	protected abstract void onCreateFragment();
	
	/**
	 * 创建完了，初始化内容
	 */
	protected abstract void onInitViewPartment();

	public boolean dispatchKeyEvent(KeyEvent event) {
		return false;
	}

	/**
	 * 当前tag
	 * @return
	 */
	public String getStepTag() {
		return this.stepTag;
	}

	/**
	 * 设置tag
	 * @return
	 */
	public void setStepTag(String stepTag) {
		this.stepTag = stepTag;
	}

	/**
	 * @return the stepTitle
	 */
	public String getStepTitle() {
		return stepTitle;
	}

	/**
	 * @param stepTitle the stepTitle to set
	 */
	public void setStepTitle(String stepTitle) {
		this.stepTitle = stepTitle;
	}
}