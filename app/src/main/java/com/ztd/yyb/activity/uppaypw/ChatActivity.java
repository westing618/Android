package com.ztd.yyb.activity.uppaypw;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.jaeger.library.StatusBarUtil;
import com.ztd.yyb.R;
import com.ztd.yyb.util.ToastUtil;

import java.util.List;

/**
 * 聊天界面
 * Created by  on 2017/4/12.
 */

public class ChatActivity   extends EaseBaseActivity {

    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_chat);

        StatusBarUtil.setColor(this, R.color.color_reb);//设置状态栏颜色

        activityInstance = this;
        //user or group id
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);//EXTRA_USER_ID
        chatFragment = new EaseChatFragment();//TODO 聊天界面
        //set arguments
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();


        getReadState();//获取语音权限


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    //语音权限
    private void getReadState() {

        if (PermissionsUtil.hasPermission(this, Manifest.permission.RECORD_AUDIO)) {


        } else {

            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {


                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                    ToastUtil.show(ChatActivity.this,"您拒绝用户使用语音权限，语音聊天将无法正常使用");
                }
            }, new String[]{Manifest.permission.RECORD_AUDIO});
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        // enter to chat activity when click notification bar, here make sure only one chat activiy
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    public String getToChatUsername(){
        return toChatUsername;
    }
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
//        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
