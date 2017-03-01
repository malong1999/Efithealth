package com.maxiaobu.healthclub.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：群资料管理
 * 伪码：
 * 待完成：
 */
public class GroupInformation extends BaseAty implements View.OnClickListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.save_group_information)
    TextView mSaveGroupInformation;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.group_summary)
    EditText mGroupSummary;
    @Bind(R.id.group_notice)
    EditText mGroupNotice;
    private EventBus mEventBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_information);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "群资料管理");
        mEventBus = EventBus.getDefault();
        mGroupSummary.setText(getIntent().getStringExtra("summary"));
        mGroupNotice.setText(getIntent().getStringExtra("notice"));
        mSaveGroupInformation.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_group_information:
                saveInfo();
                break;
        }
    }

    public void saveInfo(){
        RequestParams params = new RequestParams();
        params.put("summary",mGroupSummary.getText().toString());
        params.put("bulletin",mGroupNotice.getText().toString());
        params.put("groupid",getIntent().getStringExtra("id"));
        App.getRequestInstance().post(UrlPath.URL_GROUP_INFO, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String msgFlag = object.getString("msgFlag");
                    String msgContent = object.getString("msgContent");
                    Toast.makeText(GroupInformation.this, msgContent, Toast.LENGTH_SHORT).show();
                    if (msgFlag.equals("1")){
                        BeanEventBas beanEventBas = new BeanEventBas();
                        beanEventBas.setRefresh(true);
                        mEventBus.post(beanEventBas);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestError(VolleyError volleyError, String s) {

            }

            @Override
            public void noInternet(VolleyError volleyError, String s) {

            }
        });
    }


    /**
     * 点击事件来判断是否隐藏软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                hideSoftInput(view);
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    // 隐藏软键盘
    private void hideSoftInput(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
