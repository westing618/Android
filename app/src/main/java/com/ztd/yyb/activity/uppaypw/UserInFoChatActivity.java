package com.ztd.yyb.activity.uppaypw;

import android.os.Bundle;

import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.jaeger.library.StatusBarUtil;
import com.ztd.yyb.R;

/**
 * Created by  on 2017/5/22.
 */

public class UserInFoChatActivity extends EaseBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_userchat);

        StatusBarUtil.setColor(this, R.color.color_reb);//设置状态栏颜色

    }
}
