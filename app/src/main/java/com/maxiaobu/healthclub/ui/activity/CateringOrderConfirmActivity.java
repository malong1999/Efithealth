package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanSaveOrderInfo;
import com.maxiaobu.healthclub.common.beangson.BeanUserInfoById;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 配餐订单确认
 */
public class CateringOrderConfirmActivity extends BaseAty implements View.OnClickListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    @Bind(R.id.ly_user_info)
    LinearLayout mLyUserInfo;
    @Bind(R.id.iv_reduce)
    ImageView mIvReduce;
    @Bind(R.id.tv_food_num)
    TextView mTvFoodNum;
    @Bind(R.id.iv_add)
    ImageView mIvAdd;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_phone_num)
    TextView mTvPhoneNum;
    @Bind(R.id.tv_shipping_method)
    TextView mTvShippingMethod;
    @Bind(R.id.tv_totle_price)
    TextView mTvTotlePrice;
    @Bind(R.id.tv_totle_price_bottom)
    TextView mTvTotlePriceBottom;
    @Bind(R.id.tv_now_order)
    TextView mTvNowOrder;

    private BeanUserInfoById.BMemberBean mData;
    private int mPrice;
    private int mNum;
    private String mMerid;
    private String mPhoneNum;
    private int mTotlePrice;
    private String mRecaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering_order_confirm);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "订单确认");
        Intent intent = getIntent();
        mPrice = intent.getIntExtra("price", -1);
        mNum = intent.getIntExtra("num", -1);
        mMerid = intent.getStringExtra("merid");
        mPhoneNum = intent.getStringExtra("phoneNum");
        mTotlePrice = mPrice * mNum;
        mTvPrice.setText(String.valueOf(mPrice) + "元");
        mTvFoodNum.setText(String.valueOf(mNum));
        mTvPhoneNum.setText(mPhoneNum);
        mTvTotlePrice.setText("共计：" + String.valueOf(mTotlePrice) + "元");
        mTvTotlePriceBottom.setText("共计：" + String.valueOf(mTotlePrice) + "元");
    }

    @Override
    public void initData() {
        RequestParams para = new RequestParams("memid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(UrlPath.URL_USER_INFO_BY_ID, this, para, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                BeanUserInfoById object = JsonUtils.object(s, BeanUserInfoById.class);
//                Log.d("OrderConfirmActivity", s);
                mData = object.getBMember();
                if (mData != null) {
                    mTvName.setText("收货人：" + mData.getRecname() + " " + mData.getMobphone());
                    mRecaddress = mData.getRecaddress();
                    mTvAddress.setText("收货地址：" + mRecaddress);
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

    @OnClick({R.id.iv_reduce, R.id.iv_add, R.id.tv_now_order, R.id.ly_user_info})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_reduce:
                if (mNum > 1) {
                    mNum--;
                    mTotlePrice = mNum * mPrice;
                    mTvFoodNum.setText(String.valueOf(mNum));
                    mTvTotlePrice.setText("共计：" + String.valueOf(mTotlePrice) + "元");
                    mTvTotlePriceBottom.setText("共计：" + String.valueOf(mTotlePrice) + "元");
                }
                break;
            case R.id.iv_add:
                mNum++;
                mTotlePrice = mNum * mPrice;
                mTvFoodNum.setText(String.valueOf(mNum));
                mTvTotlePrice.setText("共计：" + String.valueOf(mTotlePrice) + "元");
                mTvTotlePriceBottom.setText("共计：" + String.valueOf(mTotlePrice) + "元");
                break;
            case R.id.ly_user_info:
//                Intent intent = new Intent();
//                intent.setClass(OrderConfirmActivity.this, RevampAddress.class);
//                Log.d("OrderConfirmActivity", "dslfnhlkdjf");
                startActivityForResult(new Intent(CateringOrderConfirmActivity.this, RevampAddress.class), 11);
                break;
            case R.id.tv_now_order:
                if (!TextUtils.isEmpty(mRecaddress)) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("memid", SPUtils.getString(Constant.MEMID));
                    requestParams.put("mernum", String.valueOf(mNum));
                    requestParams.put("ordamt", String.valueOf(mTotlePrice));
                    requestParams.put("isShopcart", "0");
                    String s = "{foodmers:[{merid:" + mMerid + ",buynum:" + mNum + "}]}";
                    requestParams.put("foodmers", s);
//                    Log.d("++", String.valueOf(mNum));
//                    Log.d("++", String.valueOf(mTotlePrice));
//                    Log.d("++", mMerid);
                    App.getRequestInstance().post(this, UrlPath.URL_SAVE_ORDER_INDO, requestParams, new RequestListener() {
                        @Override
                        public void requestSuccess(String s) {
                            Log.d("OrderConfirmActivity", s);
                            BeanSaveOrderInfo object = JsonUtils.object(s, BeanSaveOrderInfo.class);
                            if ("1".equals(object.getMsgFlag())) {
                                Intent intent = new Intent(new Intent(CateringOrderConfirmActivity.this, PayActivity.class));
                                intent.putExtra("totlePrice", String.valueOf(mTotlePrice));
                                intent.putExtra("ordno", String.valueOf(object.getOrdno()));
                                intent.putExtra(Constant.PAY_TYPE, "cater");
//                                Log.d("OrderConfirmActivity", object.getOrdno().toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(mActivity, object.getMsgContent(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mActivity, "请填写收货地址", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constant.RESULT_OK) {
            initData();
        }
    }
}
