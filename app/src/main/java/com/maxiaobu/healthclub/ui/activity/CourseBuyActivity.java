package com.maxiaobu.healthclub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanGoodsList;
import com.maxiaobu.healthclub.common.beangson.BeanMbpcourse;
import com.maxiaobu.healthclub.utils.TimesUtil;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.utils.web.BaseJsToAndroid;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：确认订单
 * 伪码：
 * 待完成：
 */
public class CourseBuyActivity extends BaseAty {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.web_view)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_buy);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        String url = getIntent().getStringExtra("url");
        Log.d("CourseBuyActivity", url);
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "确认订单");
        //http://192.168.1.121:8080/efithealth/mcoursebuysave.do?mertype=gcourse&courseid=G000052&memid=M000455
        // 设置WebView支持JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 在js中调用本地java方法
        mWebView.addJavascriptInterface(new WebAppInterface(this), "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.loadUrl("file:///android_asset/" + url);
        Log.d("Persional", "file:///android_asset/" + url);
//        mWebView.setWebViewClient(new MyWebViewClient());
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
        //跳转支付页面，进行支付操作
        @JavascriptInterface
        public void gotoPay(String ordno, String ordamt, String endDate,
                            String courseTimes, String courseName, String clubName,
                            String coachName, String coachAvatar, String coachId) {
            String page = "file:///android_asset/reservation.html?coachid="
                    + coachId + "&nickname=" + coachName + "&clubname=" +
                    clubName + "&address=1";
            page += "&coursename=" + courseName + "&enddate=" +
                    endDate + "&times=" + courseTimes + "&orderid=" + ordno;
            page += "&imgsfile=" + coachAvatar;
            Intent intent = new Intent();
            intent.putExtra("ordno", ordno);
                            Log.d("WebAppInterface", ordno);
                            Log.d("WebAppInte", ordamt);
            intent.putExtra("totlePrice", ordamt);
            intent.putExtra(Constant.PAY_TYPE, "course");
            intent.putExtra("reservation", page.trim());
            intent.setClass(CourseBuyActivity.this, PayActivity.class);
            startActivity(intent);
        }

        // 修改收货信息
        @JavascriptInterface
        public void personalInfo() {
            Intent intent = new Intent();
            intent.setClass(CourseBuyActivity.this, RevampAddress.class);
            startActivityForResult(intent, Constant.RESULT_REQUEST_ONE);
        }
        //跳转支付页面，支付团课
        @JavascriptInterface
        public void gotoPayForGcourse(String ordno, String ordamt,String meiyong ,String meiYong) {
            Log.d("WebAppInterface", ordno);
            Intent intent = new Intent();
            intent.putExtra(Constant.JUMP_KEY, Constant.GCOURSE_TO_GCOURSE);
            intent.putExtra("totlePrice", ordamt);
            intent.putExtra("ordno", ordno);
            intent.putExtra(Constant.PAY_TYPE, "course");
            SPUtils.putBoolean(Constant.FALG,true);
            intent.setClass(CourseBuyActivity.this, PayActivity.class);
            startActivityForResult(intent, Constant.RESULT_REQUEST_SECOND);
        }

        //获取memid
        @JavascriptInterface
        public String getmemid() {
            return SPUtils.getString(Constant.MEMID);
        }
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RESULT_REQUEST_ONE && resultCode == Constant.RESULT_OK) {
            mWebView.reload();
        }else if (requestCode == Constant.RESULT_REQUEST_SECOND && resultCode == RESULT_OK){
            this.finish();
        }
    }
}
