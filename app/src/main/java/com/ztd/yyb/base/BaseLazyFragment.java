package com.ztd.yyb.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.ztd.yyb.R;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

public abstract class BaseLazyFragment extends Fragment {


    protected static String TAG_LOG = null;

    /**
     * Screen information
     */
//    protected int mScreenWidth = 0;
//    protected int mScreenHeight = 0;
//    protected float mScreenDensity = 0.0f;

    protected BaseApplication baseApp;    /**
     * context
     */
    protected Activity mContext = null;

    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    //是否已完成初始化
    private boolean mIsPrepared;
    //是否已经加载一次
    private boolean mHasLoadOnce;
    private Unbinder mUnbinder;
    private View mRootView = null;// 缓存Fragment view
    private SweetAlertDialog mLoadingDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG_LOG = this.getClass().getSimpleName();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getContentViewLayoutID(), container, false);
            StatusBarUtil.setColor(getActivity(), R.color.color_reb);//设置状态栏颜色
        }
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        mUnbinder = ButterKnife.bind(this, mRootView);
        initViewsAndEvents();

        return mRootView;

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseApp = getBaseApplication();

//        Log.e("-----------------------","onViewCreated");

    }

    protected BaseApplication getBaseApplication() {
        return (BaseApplication) getActivity().getApplication();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();

//        Log.e("-----------------------","onDetach");

        // for bug ---> java.lang.IllegalStateException: Activity has been destroyed
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Log.e("-----------------------","onActivityCreated");

        initPrepare();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onUserVisible();
        } else {
            onUserInvisible();
        }
    }

    private synchronized void initPrepare() {
        if (mIsPrepared) {
            onFirstUserVisible();
        } else {
            mIsPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     * when fragment is visible for the first time, here we can do some initialized work or refresh data only once
     */
    protected abstract void onFirstUserVisible();

    /**
     * fragment可见（切换回来或者onResume）
     * this method like the fragment's lifecycle method onResume()
     */
    protected abstract void onUserVisible();

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     * when fragment is invisible for the first time
     */
    private void onFirstUserInvisible() {
        // here we do not recommend do something
    }

    /**
     * fragment不可见（切换掉或者onPause）
     * this method like the fragment's lifecycle method onPause()
     */
    protected abstract void onUserInvisible();

    /**
     * init all views and add events
     */
    protected abstract void initViewsAndEvents();

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();


    /**
     * get the support fragment manager
     *
     * @return
     */
    protected FragmentManager getSupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    protected void showToast(String msgid) {
        if (!TextUtils.isEmpty(msgid)) {
            Toast.makeText((Activity) mContext, msgid, Toast.LENGTH_LONG).show();
//            Snackbar.make(((Activity) mContext).getWindow().getDecorView(), msgid, Snackbar.LENGTH_SHORT).show();
        }
    }
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.color_reb));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText("请稍等...");
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}