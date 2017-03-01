package com.maxiaobu.healthclub.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanAccountInfo;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestJsonListener;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.mqldialoglibrary.materialdialogs.core.DialogAction;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;

/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：支付页
 * 伪码：
 * 待完成：
 */
public class PayActivity extends BaseAty implements View.OnClickListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.tv_now_order)
    TextView mTvNowOrder;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_ebi_content)
    TextView mTvEbiContent;
    @Bind(R.id.cb_e_pay)
    AppCompatCheckBox mCbEPay;
    @Bind(R.id.ly_epay)
    LinearLayout mLyEpay;
    @Bind(R.id.cb_wxin_pay)
    AppCompatCheckBox mCbWxinPay;
    @Bind(R.id.rl_wxin_pay)
    LinearLayout mRlWxinPay;
    @Bind(R.id.cb_ali_pay)
    AppCompatCheckBox mCbAliPay;
    @Bind(R.id.rl_ali_pay)
    LinearLayout mRlAliPay;
    @Bind(R.id.tv_actual_pay)
    TextView mTvActualPay;
    @Bind(R.id.rl_actual_pay)
    LinearLayout mRlActualPay;
    private String mTotlePrice;
    private int mTotalEbi;
    private String mOrdno;
    private String mPayType;
    private MaterialDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        mPayType = getIntent().getStringExtra(Constant.PAY_TYPE);
        mTotlePrice = getIntent().getStringExtra("totlePrice");
        mOrdno = getIntent().getStringExtra("ordno");
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "支付页");
        mTvPrice.setText(mTotlePrice + "元");
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams("memid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(this, UrlPath.URL_ACCOUNT_INFO,
                BeanAccountInfo.class, params, new RequestJsonListener<BeanAccountInfo>() {
                    @Override
                    public void requestSuccess(BeanAccountInfo result) {
                        mTotalEbi = (Integer.parseInt(result.getYcoincashnum()) + Integer.parseInt(result.getYcoinnum())) / 100;
                        if (mTotalEbi > Integer.parseInt(mTotlePrice)) {
                            mTvEbiContent.setText("本次可抵现" + mTotlePrice + "元，抵现后羿币余额为" + (Integer.parseInt(result.getYcoincashnum()) + Integer.parseInt(result.getYcoinnum())));
                            mTvActualPay.setText("0元");
                        } else {
                            mTvEbiContent.setText("本次可抵现" + mTotalEbi + "元，抵现后羿币余额为0");
                            mTvActualPay.setText((Integer.parseInt(mTotlePrice) - mTotalEbi) + "元");
                        }
                    }

                    @Override
                    public void requestError(VolleyError volleyError, String s) {

                    }

                    @Override
                    public void noInternet(VolleyError volleyError, String s) {

                    }

                });

       /* App.getRequestInstance().post(this, UrlPath.URL_ACCOUNT_INFO, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                BeanAccountInfo object = JsonUtils.object(s, BeanAccountInfo.class);
                mTotalEbi = (object.getYcoincashnum() + object.getYcoinnum()) / 100;
                if (mTotalEbi > Integer.parseInt(mTotlePrice)) {
                    mTvEbiContent.setText("本次可抵现" + mTotlePrice + "元，抵现后还需支付0元");
                } else {
                    mTvEbiContent.setText("本次可抵现" + mTotlePrice + "元，抵现后还需支付" + (Integer.parseInt(mTotlePrice) - mTotalEbi) + "元");
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                initData();
            }
        });*/
    }

    @OnClick({R.id.ly_epay, R.id.rl_wxin_pay, R.id.rl_ali_pay, R.id.tv_now_order})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_epay:
                mCbEPay.setChecked(!mCbEPay.isChecked());
                break;
            case R.id.rl_wxin_pay:
                Toast.makeText(this, "微信支付未开通", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_ali_pay:
                Toast.makeText(this, "支付宝未开通", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_now_order:
                pay();
                break;
            default:
                break;
        }
    }

    private void pay() {
        if (mCbEPay.isChecked()) {
//            Log.d("PayActivity", mOrdno);
            if (mTotalEbi > Double.parseDouble(mTotlePrice)) {
                //仅 e币
                RequestParams params;
                String url;
                if (mPayType != null && mPayType.equals("course")) {
                    params = new RequestParams("ordno", mOrdno);
                    params.put("memid", SPUtils.getString(Constant.MEMID));
                    url = UrlPath.URL_COURSE_EBI_PAY;
                } else {
                    params = new RequestParams("ordno", "{\"ordno\":" + mOrdno + "}");
                    url = UrlPath.URL_EBI_PAY;
                }
                //CO-20160905-994
                Log.d("++", mOrdno);
                App.getRequestInstance().post(this, url, params, new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {
                        try {
                            JSONObject a = new JSONObject(s);
                            if (a.get("msgFlag").equals("1")) {
                                startAty();
                            } else {
                                Toast.makeText(PayActivity.this, a.get("msgContent").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(mActivity, "接口是不是改了", Toast.LENGTH_SHORT).show();
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
            } else {
                Toast.makeText(mActivity, "余额不足以支付", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
        }
    }


    private void payRequest() {


    }

    //支付成功后弹窗
    private void startAty() {
        if (!TextUtils.isEmpty(mPayType) && mPayType.equals("course")) {
            mDialog = new MaterialDialog.Builder(PayActivity.this)
                    .content("支付成功，是否现在预约")
                    .positiveText("现在预约")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (getIntent().getIntExtra(Constant.JUMP_KEY, -1) == Constant.GCOURSE_TO_GCOURSE) {
                                PayActivity.this.setResult(RESULT_OK);
                                PayActivity.this.finish();
                            } else {
                                Intent intent = new Intent();
                                intent.setClass(PayActivity.this, ReservationActivity.class);
                                intent.putExtra("coachid", getIntent().getStringExtra("coachid"));
                                intent.putExtra("orderid", getIntent().getStringExtra("ordno"));
                                intent.putExtra("reservation", getIntent().getStringExtra("reservation"));
                                intent.putExtra(Constant.JUMP_KEY, Constant.PAY_TO_RESERVATION);
                                startActivity(intent);
                                PayActivity.this.finish();
                            }

                        }
                    })
                    .negativeText("稍后预约")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(Constant.JUMP_KEY, Constant.PCOURSE_TO_PCORDER);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setClass(PayActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }).canceledOnTouchOutside(false).keyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (mDialog.isShowing()&&keyCode==KeyEvent.KEYCODE_BACK)
                                mDialog.dismiss();
                                PayActivity.this.finish();
                            return false;
                        }
                    })
                    .show();
        } else {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(PayActivity.this, HomeActivity.class);
            intent.putExtra(Constant.JUMP_KEY, Constant.CATERINGDETAIL_TO_PCORDER);
            startActivity(intent);
        }
    }
}
