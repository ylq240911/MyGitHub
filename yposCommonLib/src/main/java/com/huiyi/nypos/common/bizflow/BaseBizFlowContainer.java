package com.huiyi.nypos.common.bizflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.huiyi.nypos.common.uistep.UIContainerActivity;
import com.huiyi.nypos.common.utils.ActivityCollector;



public abstract class BaseBizFlowContainer extends  UIContainerActivity{
	
	private BizFlowData bizFlowData =new BizFlowData();

	public BizFlowData getBizFlowData(){
		return bizFlowData;
	}
	
	public void setBizFlowData(BizFlowData bizFlowData){
		this.bizFlowData=bizFlowData;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		ActivityCollector.addActivity(this);				
		bizFlowData.setBizType(getBizTypeFromIntent());
		
		super.onCreate(savedInstanceState);	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setFull(false);
	}
	
	/**
	 * 获取intent bizType
	 * @return
	 */
	protected String getBizTypeFromIntent() {
		Intent intent = getIntent();
		String bizType = intent.getStringExtra("BUSINESS_NAME");
		return bizType==null ? "NO_BUSINESS" : bizType;
	}
	
	public abstract View getBackButton();
	
	public abstract TextView getTitleTextView();
	
	public void finishStepResult(int resultCode) {
		setResult(resultCode);
		finish();
	}
	
	public void finishStepResult(int resultCode,Intent data) {
		setResult(resultCode,data);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		ActivityCollector.removeActivity(this);
		super.onDestroy();
	}
	
	/*@Override
	public void onAttachedToWindow() {
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
		super.onAttachedToWindow();
	}*/
		
	/**
	 * 设置是否改变状态栏的颜色
	 * @param enable
	 */
	public void setFull(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
	      WindowManager.LayoutParams lp = getWindow().getAttributes();
	      lp.flags = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
	      getWindow().setAttributes(lp);
	      getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }
	
	/**
	 * 设置页面显示标题
	 * @param title
	 */
	public void setDispayTitle(String title){
		if(getTitleTextView()!=null)
			getTitleTextView().setText(title);
	}
	
	/**
	 * 设置回退按钮是否显示
	 * @param visibility
	 */
	public void setBackButtonVisable(int visibility ){
		if(getBackButton()!=null)
			getBackButton().setVisibility(visibility);
	}
	
}
