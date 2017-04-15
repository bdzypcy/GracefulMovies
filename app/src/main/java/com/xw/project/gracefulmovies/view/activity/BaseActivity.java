package com.xw.project.gracefulmovies.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.oubowu.slideback.SlideBackHelper;
import com.oubowu.slideback.SlideConfig;
import com.oubowu.slideback.callbak.OnSlideListener;
import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.R;

import org.polaric.colorful.ColorfulActivity;

/**
 * BaseActivity
 * <p/>
 * Created by woxingxiao on 2017-02-06.
 */

public abstract class BaseActivity extends ColorfulActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private boolean hasIntentionToSlideBack; // 具有触发滑动返回的意图

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeSlideBack();
    }

    /**
     * 初始化滑动返回
     */
    private void initializeSlideBack() {
        SlideBackHelper.attach(
                this, // 当前Activity
                GMApplication.getActivityHelper(), // Activity栈管理工具
                new SlideConfig.Builder() // 参数的配置
                        .rotateScreen(false) // 屏幕是否旋转
                        .edgeOnly(true) // 是否侧滑
                        .lock(false) // 是否禁止侧滑
                        .edgePercent(0.2f) // 边缘滑动的响应阈值，0~1，对应屏幕宽度*percent
                        .slideOutPercent(0.5f) // 关闭页面的阈值，0~1，对应屏幕宽度*percent
                        .create(),
                new OnSlideListener() { // 滑动的监听
                    @Override
                    public void onSlide(@FloatRange(from = 0.0, to = 1.0) float percent) {
                        if (!hasIntentionToSlideBack && percent > 0.0) {
                            hasIntentionToSlideBack = true;
                            onSlideBackIntention();
                        }
                    }

                    @Override
                    public void onOpen() {
                        hasIntentionToSlideBack = false;
                    }

                    @Override
                    public void onClose() {

                    }
                }
        );
    }

    /**
     * Activity跳转导航
     *
     * @param activity 目标Activity.class
     */
    protected void navigateTo(Class activity) {
        startActivity(new Intent(this, activity));
    }

    /**
     * 检测系统版本并使状态栏全透明
     */
    protected void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS); // 新增滑动返回，舍弃过渡动效

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 初始化Toolbar的功能
     */
    protected void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 当有滑动返回的意图时
     */
    protected void onSlideBackIntention() {
    }
}
