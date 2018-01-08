package com.ztd.yyb.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ztd.yyb.R;


/**
 * 自定义对话框,要使用本窗体，请先继承CreateDialogImpl接口，并实现自己的确定与取消的事件
 *
 * @author wudh
 *
 */
public abstract class CreateDialog {
	// private Activity ac;
	private int layouts = R.layout.view_dialog;// 布局文件,可修改
	private int okId = R.id.re_left, cannerId = R.id.re_right;// 布局文件,确定和取消
	//private CreateDialogImpl mCreateDialogImpl;// 接口
	private String strTitle;
	private View tvOK;
	private View layout;
	private Dialog dialog;
	private TextView tvTitle;
	private TextView tvOKStr;
	private TextView tvNo;
	/**
	 * 带自定义标题创建
	 *
	 * @param ac
	 * @param title
	 */
	public CreateDialog(Activity ac, String title,String btnLeft ,String btnRight) {
		setStrTitle(title);
		initView(ac);
		tvTitle.setText(title);
		tvOKStr.setText(btnLeft);
		tvNo.setText(btnRight);
		showDialog(ac);
		setDialog();


	}

	/**
	 * 带布局中默认标题创建
	 *
	 * @param ac
	 */
	public CreateDialog(Activity ac) {
		initView(ac);

		showDialog(ac);
		setDialog();


	}

	/**
	 * 初始化
	 *
	 * @param ac
	 */
	private void initView(Activity ac) {
		LayoutInflater inflater = LayoutInflater.from(ac);
		layout = inflater.inflate(getLayouts(), null);
		tvTitle = (TextView) layout.findViewById(R.id.tv_title);
		tvOK = layout.findViewById(getOkId());
		tvOKStr=(TextView)layout.findViewById(R.id.tv_yes);
		tvNo=(TextView)layout.findViewById(R.id.tv_no);
	}

	/**
	 * 创建并显示对话框
	 *
	 * @param ac
	 */
	private void showDialog(Activity ac) {
		dialog = new AlertDialog.Builder(ac).create();
		dialog.show();
	}
	public abstract void onEnterListener();
	public abstract void onCancelListener();
	/**
	 * 设置对话框
	 */
	private void setDialog() {
		dialog.getWindow().setContentView(layout);

		tvOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				onEnterListener();
			}
		});
		View tvCancel = layout.findViewById(getCannerId());
		tvCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				onCancelListener();
			}
		});

	}

	public int getLayouts() {
		return layouts;
	}

	public void setLayouts(int layouts) {
		this.layouts = layouts;
	}

	public int getOkId() {
		return okId;
	}

	public void setOkId(int okId) {
		this.okId = okId;
	}

	public int getCannerId() {
		return cannerId;
	}

	public void setCannerId(int cannerId) {
		this.cannerId = cannerId;
	}

	public String getStrTitle() {
		return strTitle;
	}

	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}
/*	public interface CreateDialogImpl {
		
		public void enterMethod();

		
	}*/
}
