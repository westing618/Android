package com.ztd.yyb.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ztd.yyb.R;

/**
 * Created by  on 2017/3/21.
 */

public class MyProgressDialog extends BaseDialog {

    private static MyProgressDialog myProgressDialog;
    private Context context;
    public static void showDialog(Context context) {
        myProgressDialog = new MyProgressDialog(context);
        myProgressDialog.show();
    }

    public static void dismissDialog() {
        if (myProgressDialog != null) {
            myProgressDialog.dismiss();
            myProgressDialog.cancel();
            myProgressDialog = null;
        }
    }

    public MyProgressDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
        this.setCancelable(false);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_progress, null);
        this.setContentView(view);
    }
}
