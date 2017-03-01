package com.maxiaobu.healthclub.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.ui.activity.CateringDetailActivity;
import com.maxiaobu.healthclub.ui.activity.ClubDetailActivity;
import com.maxiaobu.healthclub.ui.activity.ClubListActivity;
import com.maxiaobu.healthclub.ui.activity.CourseBuyActivity;
import com.maxiaobu.healthclub.ui.activity.HomeActivity;
import com.maxiaobu.healthclub.ui.activity.MyBespeakActivity;
import com.maxiaobu.healthclub.ui.activity.PayActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.activity.RevampAddress;
import com.maxiaobu.healthclub.ui.activity.WebActivity;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.utils.web.BaseJsToAndroid;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.maxiaobu.healthclub.R.id.webview;

/**
 * 通用web frg
 */
public class WebFragment extends BaseFrg {


    @Bind(R.id.web_view)
    WebView mWebView;

    public WebFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 在js中调用本地java方法
        mWebView.addJavascriptInterface(new WebAppInterface(getActivity()), "mobile");
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        Log.d("WebFragment", getArguments().getString("url"));
        if (getArguments().getBoolean("software")&& Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP)
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.loadUrl(getArguments().getString("url"));
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
        public void gotoClubList() {
            startActivity(new Intent(getActivity(), ClubListActivity.class));
        }


        @JavascriptInterface
        public void gotoEssayList() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HomeActivity activity= (HomeActivity) getActivity();
                    activity.mBottomNavigationBar.selectTab(2);
                }
            });
        }

        @JavascriptInterface
        public void gotoBespeakList() {
            startActivity(new Intent(getActivity(), MyBespeakActivity.class));
        }
        //parmType: 1:个人主页, 2:配餐单页, 3:webview;
//        parm:个人主页所需参数, 配餐单页所需参数, url;
        //1:memrole   tarid  (俱乐部)tarid
        //2:merid
        //3:url 一半
        @JavascriptInterface
        public void gotoNewWindow(String parmType, String parm) {
            Intent intent=new Intent();
            if (parmType.equals("1")){
                try {
                    JSONObject object = new JSONObject(parm);
                    String memrole = object.getString("memrole");
                    String tarid = object.getString("tarid");
                    intent.putExtra("memrole",memrole);
                    intent.putExtra("tarid",tarid);
                    if (memrole.equals("clubadmin")){
                        intent.setClass(getActivity(), ClubDetailActivity.class);
                    }else {
                        intent.setClass(getActivity(), PersionalActivity.class);
                    }
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "gotoNewWindow的parm参数json解析失败", Toast.LENGTH_SHORT).show();
                }
            }
            if (parmType.equals("2")){
                intent.putExtra("merid",parm);
                intent.setClass(getActivity(), CateringDetailActivity.class);
                startActivity(intent);
            }
            if (parmType.equals("3")){
                intent.putExtra("url","file:///android_asset/"+parm);
                intent.setClass(getActivity(), WebActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
