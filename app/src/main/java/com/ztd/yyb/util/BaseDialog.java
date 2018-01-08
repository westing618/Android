package com.ztd.yyb.util;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;

public class BaseDialog extends Dialog {

	public BaseDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		// 按返回键，取消对话框
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.dismiss();
		}

		return super.onKeyDown(keyCode, event);
	}

}
