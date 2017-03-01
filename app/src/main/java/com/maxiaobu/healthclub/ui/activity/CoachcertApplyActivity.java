package com.maxiaobu.healthclub.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：申请认证
 * 伪码：
 * 待完成：
 */
public class CoachcertApplyActivity extends BaseAty {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.ed_name)
    AppCompatEditText mEdName;
    @Bind(R.id.ed_price)
    AppCompatEditText mEdPrice;
    @Bind(R.id.ed_introduction)
    EditText mEdIntroduction;
    @Bind(R.id.ed_describe)
    AppCompatEditText mEdDescribe;
    @Bind(R.id.tv_send)
    TextView mTvSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coachcert_apply);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "申请认证");
        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdName.getText().toString().equals("") || mEdPrice.getText().toString().equals("") || mEdIntroduction.getText().toString().equals("") || mEdDescribe.getText().toString().equals("")) {
                    Toast.makeText(CoachcertApplyActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    // http://192.168.1.182:8080/efithealth/mcoachcertApply.do?memid=M000439&nickname=马小布&coachprice=100&coachadept=快捷方式的恢复开机后&applydescr=的萨科罚金电视里看见弗兰克
                    RequestParams params = new RequestParams();
                    params.put("memid", SPUtils.getString(Constant.MEMID));
                    params.put("nickname", mEdName.getText().toString());
                    params.put("coachprice", mEdPrice.getText().toString());
                    params.put("coachadept", mEdIntroduction.getText().toString());
                    params.put("applydescr", mEdDescribe.getText().toString());
                    App.getRequestInstance().post(CoachcertApplyActivity.this,
                            UrlPath.URL_MCOACHCERTAPPLY, params, new RequestListener() {
                                @Override
                                public void requestSuccess(String s) {
                                    try {
                                        JSONObject data = new JSONObject(s);
                                        Object msgFlag = data.get("msgFlag");
                                        Toast.makeText(CoachcertApplyActivity.this, data.get("msgContent").toString(), Toast.LENGTH_SHORT).show();
                                        if (msgFlag.equals("1")) {
                                            CoachcertApplyActivity.this.finish();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(CoachcertApplyActivity.this, "json解析失败", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    @Override
    public void initData() {

    }
}
