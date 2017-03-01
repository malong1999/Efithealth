package com.maxiaobu.healthclub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.utils.web.BaseJsToAndroid;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/12/14.
 * email：maxiaobu1216@gmail.com
 * 功能：我的会员卡
 * 伪码：
 * 待完成：
 */
public class MyCardActivity extends BaseAty {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean san2order = getIntent().getBooleanExtra("san2order", false);
        if (san2order) {
            startActivity(new Intent(this, OrderListActivity.class));
        } else {
            setContentView(R.layout.activity_mine_card);
            ButterKnife.bind(this);
            initView();
            initData();
        }


    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MyCardActivity", "getIntent() false):" + getIntent().getBooleanExtra("san2order", false));
        if (getIntent().getBooleanExtra("san2order", false)) {
            setContentView(R.layout.activity_mine_card);
            ButterKnife.bind(this);
            initView();
            initData();
        }
    }
    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"会员卡");
    }

    @Override
    public void initData() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.loadUrl("file:///android_asset/memberCard.html");
    }

    class WebAppInterface extends BaseJsToAndroid{

        Context mContext;
        public WebAppInterface(Context context) {
            super(context);
            mContext = context;
        }

        @JavascriptInterface
        public String getmemid(){
            return SPUtils.getString(Constant.MEMID);
        }
    }
}
