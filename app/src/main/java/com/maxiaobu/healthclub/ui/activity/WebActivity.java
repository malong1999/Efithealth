package com.maxiaobu.healthclub.ui.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.utils.web.BaseJsToAndroid;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 通用web  aty
 */
public class WebActivity extends BaseAty implements View.OnClickListener {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.web_view)
    WebView mWebView;
    @Bind(R.id.activity_web)
    RelativeLayout mActivityWeb;
    @Bind(R.id.image_share)
    ImageView mImageShare;
    @Bind(R.id.image_share_layout)
    RelativeLayout mImageShareLayout;

    private Boolean open = false;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 在js中调用本地java方法
        mWebView.addJavascriptInterface(new WebAppInterface(this), "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        Log.d("WebFragment", getIntent().getStringExtra("url"));
        mWebView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    public void initData() {
        open = getIntent().getBooleanExtra("open", false);
        if (open) {
            mImageShare.setVisibility(View.VISIBLE);
            mImageShareLayout.setOnClickListener(this);
        } else {
            mImageShare.setVisibility(View.GONE);
        }
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    @Override
    public void onClick(View v) {
//                    UmengTool.getSignature(this);
        ShareBoardConfig config = new ShareBoardConfig();
        config.setIndicatorColor(0x00E9EFF2, 0xffE9EFF2);

        new ShareAction(WebActivity.this)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                        , SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL)
                .addButton("umeng_sharebutton_custom", "umeng_sharebutton_custom", "info_icon_1", "info_icon_1")
                .setShareboardclickCallback(shareBoardlistener)
                .open(config);
//            Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
    }

    public class WebAppInterface extends BaseJsToAndroid {
        Context mContext;

        WebAppInterface(Context c) {
            super(c);
            mContext = c;
        }

        @JavascriptInterface
        public void gotoClubList() {
            startActivity(new Intent(mActivity, ClubListActivity.class));
        }


        @JavascriptInterface
        public void gotoEssayList() {
            HomeActivity activity = (HomeActivity) mContext;
            activity.mBottomNavigationBar.selectTab(2);
            startActivity(new Intent(mActivity, ClubListActivity.class));
        }
    }

    /*****************************************
     * 莫小婷：友盟分享
     ***********************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    /***
     * 分享面板点击监听
     */
    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media == null) {
                if (snsPlatform.mKeyword.equals("umeng_sharebutton_custom")) {
                    myClip = ClipData.newPlainText("text", "https://www.baidu.com/");
                    myClipboard.setPrimaryClip(myClip);
                    Toast.makeText(WebActivity.this, "复制成功", Toast.LENGTH_LONG).show();
                }

            } else {
                new ShareAction(WebActivity.this).setPlatform(share_media)
                        .withTitle("efitHeath分享")
                        //必须写，否者出不来
                        /**
                         * 打包成功的友盟分享的微信出不来：
                         *         1、必须写出withText()
                         */
                        .withText("百度一下，你就知道")
                        .withTargetUrl("https://www.baidu.com/")  //分享http://或者https://
                        .setCallback(umShareListener)
                        .share();
            }
        }
    };

    /**
     * 分享监听
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(WebActivity.this, "分享成功", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(WebActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(WebActivity.this, "分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


}
