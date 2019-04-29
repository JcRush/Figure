package com.example.figurechatcommunity.base;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.figurechatcommunity.Bean.UserInfo;
import com.example.figurechatcommunity.BmobIMApplication;
import com.example.figurechatcommunity.Config;

import org.greenrobot.eventbus.EventBus;

import java.util.logging.Logger;

public class BaseActivity extends FragmentActivity {
    /**
     * 屏幕宽高
     */
    public int mScreenWidth;
    public int mScreenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取屏幕宽高
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //ButterKnife.bind(this);
        initView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        //ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ButterKnife.unbind(this);
    }

    //@Subscribe
    //public void onEvent(Boolean empty) {

    //}

    protected void initView() {
    }

    protected void runOnMain(Runnable runnable) {
        runOnUiThread(runnable);
    }

    protected final static String NULL = "";
    private Toast toast;

    public void toast(final Object obj) {
        try {
            runOnMain(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(BaseActivity.this, NULL, Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            finish();
    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘-一般是EditText.getWindowToken()
     *
     * @param token
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Log日志
     *
     * @param msg
     */
    public void log(String msg) {
        if (Config.DEBUG) {
            Log.d("msg",msg);
        }
    }


    /**
     * 用于登陆或者自动登陆情况下的用户资料及好友资料的检测更新
     *
     * @param
     * @return void
     * @throws
     * @Title: updateUserInfos
     * @Description: TODO
     */
    public void updateUserInfos() {
        //更新地理位置信息
        updateUserLocation();
    }

    /**
     * 更新用户的经纬度信息
     *
     * @param
     * @return void
     * @throws
     * @Title: uploadLocation
     * @Description: TODO
     */
    public void updateUserLocation() {
        UserInfo u = (UserInfo) BmobUser.getCurrentUser(UserInfo.class);
        u.setLocation(BmobIMApplication.lastPoint);
        u.setObjectId(u.getObjectId());
        u.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    log("经纬度更新成功");
                }else{
                    e.printStackTrace();
                    log("经纬度更新失败"+e.getMessage());

                }
            }

        });


    }

}

