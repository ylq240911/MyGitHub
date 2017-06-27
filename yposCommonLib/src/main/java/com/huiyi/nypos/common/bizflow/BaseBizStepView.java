package com.huiyi.nypos.common.bizflow;

import com.huiyi.nypos.common.uistep.BaseUIStepView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


public abstract class BaseBizStepView extends BaseUIStepView {

	private int screenWidth = 0;
	private int screenHeight = 0;
	private boolean isFirstResume = true;
	private boolean isKeyFunctionDown=false;
	
	protected BizFlowData getBizFlowData() {
		return ((BaseBizFlowContainer) getStepActivity()).getBizFlowData();
	}
	
	protected Object getBizFlowParam(String key) {
		return ((BaseBizFlowContainer) getStepActivity()).getBizFlowData().getFlowParams().getValue(key);
	}
	
	protected void setBizFlowParam(String key,Object objValue) {
		((BaseBizFlowContainer) getStepActivity()).getBizFlowData().getFlowParams().setValue(key, objValue);
	}	

	public BaseBizStepView() {
		super();
	}

	public BaseBizStepView(int resourceId) {
		super(resourceId);
	}

	@Override
	protected void onCreateFragment() {
		
		getWidthAndLenght();
	}
	
	protected void startBizFlow(String bizType,
			Class<? extends BaseBizFlowContainer> containerActivity,
			int activityLayout, int fragmentLayout, Bundle param) {
		Intent intent = new Intent(getActivity(), containerActivity);
		intent.putExtra("BUSINESS_NAME", bizType);
		intent.putExtra(BaseBizFlowContainer.ACTIVITY_LAYOUT_RESOURCE,
				activityLayout);
		intent.putExtra(BaseBizFlowContainer.FRAGMENT_RESOURCE, fragmentLayout);
		if (param != null) {
			intent.putExtras(param);
		}
		startActivityForResult(intent, 0);
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	protected void getWidthAndLenght() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	@Override
	public void onAttach(Activity activity) {		
		super.onAttach(activity);		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setStepDisplayTitle(this.getStepTitle());
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onResume() {
		super.onResume();
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			if (getActivity() != null
					&& getActivity().getCurrentFocus() != null
					&& getActivity().getCurrentFocus().getWindowToken() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity()
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
		if (isFirstResume) {
			isFirstResume = false;
			onFirstResume();
		}
	}

	@Override
	public void onPause() {
		hideIME();
		super.onPause();
	}

	protected void hideIME() {
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			if (getActivity() != null
					&& getActivity().getCurrentFocus() != null
					&& getActivity().getCurrentFocus().getWindowToken() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity()
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	protected void onFirstResume() {
	}

	protected void onKeyBack() {
		// PosLogUtil.logi(TAG,"-------------keyback------------");
		backStep();		
	}

	protected void onKeyFunction() {
		onKeyScan();
	}

	protected void onKeyF1() {
	}

	protected void onKeyF2() {
	}

	protected void onKeyF3() {
	}

	protected void onKeyF4() {
	}

	protected void onKeyAdd() {
	}

	protected void onKeyScan() {
	}
	
	public void doFunctionAction(int keycode) {
	}
	/**
	 * 设置步骤显示标题
	 * @param title
	 */
	protected void setStepDisplayTitle(String title){
		((BaseBizFlowContainer) getStepActivity()).setDispayTitle(title);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (action) {
		case KeyEvent.ACTION_DOWN: {
			switch (keyCode) {
			case KeyEvent.KEYCODE_FUNCTION:
				isKeyFunctionDown = true;
				return true;
			case KeyEvent.KEYCODE_0:
			case KeyEvent.KEYCODE_1:
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_3:
			case KeyEvent.KEYCODE_4:
			case KeyEvent.KEYCODE_5:
			case KeyEvent.KEYCODE_6:
			case KeyEvent.KEYCODE_7:
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_9: {
				if (isKeyFunctionDown) {
					doFunctionAction(keyCode);
					isKeyFunctionDown = false;
				} else {
					return super.dispatchKeyEvent(event);
				}
			}
				return true;
			default:
				break;
			}
		}
			break;
		case KeyEvent.ACTION_UP: {
			switch (keyCode) {
			case KeyEvent.KEYCODE_FUNCTION:
				onKeyFunction();
				isKeyFunctionDown = false;
				return true;
			case KeyEvent.KEYCODE_CAMERA:
				return true;
			case KeyEvent.KEYCODE_FOCUS:
				onKeyScan();
				return true;
			case KeyEvent.KEYCODE_ENTER:
				onKeyNext();
				return true;
			case KeyEvent.KEYCODE_BACK:
				onKeyBack();
				return true;
			case KeyEvent.KEYCODE_F1:
				onKeyF1();
				return true;
			case KeyEvent.KEYCODE_F2:
				onKeyF2();
				return true;
			case KeyEvent.KEYCODE_F3:
				onKeyF3();
			case KeyEvent.KEYCODE_F4:
				onKeyF4();
				return true;
			default:
				break;
			}
		}
			break;
		default:
			break;
		}
		return super.dispatchKeyEvent(event);
	}
	
	public void toast(String msg){
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}
	
}
