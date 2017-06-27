package com.huiyi.nypos.common.listener;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 自定义点击事件
 * 用途：
 * 1.防止重复点击 <5s
 * 
 * @author adam
 *
 */
public abstract class OnCustomClickListener implements OnClickListener {
	
	public static final int MIN_CLICK_DELAY_TIME = 5000;  //5s之内不能重复点击
    private long lastClickTime = 0;
    
	protected abstract void onCustomClick(View v);
	
	@Override
	public void onClick(View v) {		
		 long currentTime = System.currentTimeMillis();
         if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
             lastClickTime = currentTime;
             onCustomClick(v);
         }
	}

}
