package com.maxiaobu.healthclub.ui.activity;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.utils.web.BaseJsToAndroid;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.mqldialoglibrary.materialdialogs.core.DialogAction;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：申请绑定俱乐部
 * 伪码：
 * 待完成：
 */
public class ApplyBindClubActivity extends BaseAty implements EasyPermissions.PermissionCallbacks{
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_bind_club);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "申请绑定");
        // 设置WebView支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 在js中调用本地java方法
        mWebView.addJavascriptInterface(new WebAppInterface(this), "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setWebViewClient(new MyWebViewClient());
//        clubid=C000174&coachid=M000447
//        Log.d("ApplyBindClubActivity", "file:///android_asset/bindClub.html?clubid=" + getIntent().getStringExtra("tarclubidid") + "&coachid=" + getIntent().getStringExtra("coachid"));
        mWebView.loadUrl("file:///android_asset/bindClub.html?clubid=" +getIntent().getStringExtra("clubid")+"&coachid="+getIntent().getStringExtra("coachid"));
//        Log.d("ApplyBindClubActivity", "file:///android_asset/bindClub.html?clubid=" + getIntent().getStringExtra("clubid") + "&coachid=" + getIntent().getStringExtra("coachid"));
    }

    @Override
    public void initData() {
    }

    public class WebAppInterface extends BaseJsToAndroid {
        Context mContext;

        WebAppInterface(Context c) {
            super(c);
            mContext = c;
        }

        @JavascriptInterface
        public void backExe() {
            ApplyBindClubActivity.this.finish();
        }
        @JavascriptInterface
        public void callUp(final String phoneNumber) {
            ringUp(phoneNumber);
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 页面加载完成 通过js回调函数实现获取页面信息
            view.loadUrl("javascript:window.mobile.loadPageData("
                    + "document.title,$('title').attr('isback'),$('title').attr('btn'),$('title').attr('navbar'))");
        }
    }

    /**
     * 打电话确认
     */
    @AfterPermissionGranted(122)
    private void ringUp(final String phoneNum) {
        new MaterialDialog.Builder(this)
                .title("呼叫")
                .content(phoneNum)
                .positiveColor(getResources().getColor(R.color.colorTextPrimary))
                .positiveText("确认")

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        if (EasyPermissions.hasPermissions(ApplyBindClubActivity.this, Manifest.permission.CALL_PHONE)) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + phoneNum);
                            intent.setData(data);
                            startActivity(intent);
                        } else {
                            EasyPermissions.requestPermissions(ApplyBindClubActivity.this, "需要打电话的权限",
                                    122, Manifest.permission.CALL_PHONE);//让easyPermission去请求权限
                        }
                    }
                })
                .negativeColor(getResources().getColor(R.color.colorTextPrimary))
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 没有需要设置的地方，复制就行，但必须有
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions   将结果转发给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    /**
     * 用户给权限了
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        Log.i(TAG, "onPermissionsDenied: "+ requestCode + ":" + perms.size());
    }
    /**
     * 权限被拒绝胃
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, "你妈的，瞎恩个jb，点设置---》权限---》权限给我",R.string.setting,R.string.cancel,perms);
    }
}
