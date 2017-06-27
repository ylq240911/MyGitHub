package com.huiyi.nypos.common.bizflow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.huiyi.nypos.common.utils.ActivityCollector;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ActivityCollector.addActivity(this);
		super.onCreate(savedInstanceState);
	}

	// 隐藏软键盘
	public void hideInputManager() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (!imm.isActive()) {
			return;
		}
		if (getSystemService(INPUT_METHOD_SERVICE) != null
				&& this.getCurrentFocus() != null) {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(this.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	public void showInputManager(View v) {

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, 0);

	}

}
