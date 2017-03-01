package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import java.util.Timer;
import java.util.TimerTask;

import maxiaobu.mxbutilscodelibrary.AppUtils;

/**
 * 开屏页
 * 设置动画 --guide-登录--主页面--更新--初始化环信
 */
public class SplashActivity extends AppCompatActivity {
    private static final int sleepTime = 500;
    private Timer mTimer;
    private ImageView iv_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final long start = System.currentTimeMillis();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (DemoHelper.getInstance().isLoggedIn() && null != SPUtils.getString(Constant.MEMID)) {
                    long costTime = System.currentTimeMillis() - start;
                    //没到两秒等两秒
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //进入主界面
                    goHomeActivity();
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    //没登录过进入登录界面
                    checkGuide();
                }
            }
        }, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    private void checkGuide() {
        if (!SPUtils.getBoolean(Constant.HAS_GUIDE)) {
            SPUtils.putBoolean(Constant.HAS_GUIDE, true);
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            finish();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            finish();
        }
    }

    private void goHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }
}
