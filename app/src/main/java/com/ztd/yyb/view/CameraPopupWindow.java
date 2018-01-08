package com.ztd.yyb.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.ztd.yyb.R;


public class CameraPopupWindow extends PopupWindow {
	private View mMenuView;
	private Activity context;

	public CameraPopupWindow(Activity context, OnClickListener mClickListener) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.camera_popup, null);
		// ����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		// ����SelectPicPopupWindow��������Ŀ�?
		this.setWidth(LayoutParams.MATCH_PARENT);
		// ����SelectPicPopupWindow��������ĸ�?
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		// ����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.AnimBottom);
		// ʵ��һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);
		// ����SelectPicPopupWindow��������ı���?
		this.setBackgroundDrawable(dw);
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = 1f; // 0.0-1.0
		context.getWindow().setAttributes(lp);
		mMenuView.findViewById(R.id.camera_popup_btn_cancel)
				.setOnClickListener(mClickListener);
		mMenuView.findViewById(R.id.camera_popup_btn_takecamera)
				.setOnClickListener(mClickListener);
		mMenuView.findViewById(R.id.camera_popup_btn_selectimg)
				.setOnClickListener(mClickListener);

	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		// TODO Auto-generated method stub
		receiverKeyboard(context, parent);
		ColorDrawable cd = new ColorDrawable(0x000000);
		this.setBackgroundDrawable(cd);
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);
		super.showAtLocation(parent, gravity, x, y);
	}
	public static void receiverKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.alpha = 1f;
		context.getWindow().setAttributes(lp);
	}

}
