package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanFoodOrderDetail;
import com.maxiaobu.healthclub.ui.weiget.NoDoubleClickListener;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：订餐订单详情
 * 伪码：
 * 待完成：
 */
public class FoodOrderDetailActivity extends BaseAty {


    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_food_name)
    TextView mTvFoodName;
    @Bind(R.id.tv_food_num)
    TextView mTvFoodNum;
    @Bind(R.id.tv_food_time)
    TextView mTvFoodTime;
    @Bind(R.id.tv_food_shipping)
    TextView mTvFoodShipping;
    @Bind(R.id.tv_food_userAddress)
    TextView mTvFoodUserAddress;
    @Bind(R.id.tv_food_userName)
    TextView mTvFoodUserName;
    @Bind(R.id.tv_food_userPhone)
    TextView mTvFoodUserPhone;
    @Bind(R.id.tv_food_nextTime)
    TextView mTvFoodNextTime;
    @Bind(R.id.tv_food_money)
    TextView mTvFoodMoney;
    @Bind(R.id.next_button)
    Button mNextButton;
    @Bind(R.id.rl_layout8)
    MaterialRippleLayout mRlLayout;


    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "订单详情");
        mNextButton.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                Intent intent = new Intent(FoodOrderDetailActivity.this,
                        CateringDetailActivity.class);
                intent.putExtra("merid", getIntent().getStringExtra("merid"));
                startActivity(intent);
            }
        });

        mRlLayout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                Intent intent = new Intent(FoodOrderDetailActivity.this, DistributionDetailActivity.class);
                intent.putExtra("str", str);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("ordno", getIntent().getStringExtra("ordno"));
        Log.d("FoodOrderDetailActivity", UrlPath.URL_ODR_DETAIL + "?ordno=" + getIntent().getStringExtra("ordno"));
        App.getRequestInstance().post(UrlPath.URL_ODR_DETAIL, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                BeanFoodOrderDetail result = new Gson().fromJson(json, BeanFoodOrderDetail.class);
                mTvTitle.setText(result.getBForder().getOrdstatusname());
                mTvFoodName.setText(result.getMerList().get(0).getMername() + " :");
                mTvFoodNum.setText("x" + result.getMerList().get(0).getBuynum());
                mTvFoodTime.setText(result.getBForder().getOrdtimestr());
                mTvFoodShipping.setText(result.getBForder().getDisttypename());
                mTvFoodUserAddress.setText(result.getBForder().getRecaddress());
                mTvFoodUserName.setText(result.getBForder().getRecname());
                mTvFoodUserPhone.setText(result.getBForder().getRecphone());
                mTvFoodMoney.setText("共计：" + result.getMerList().get(0).getMerprice() + "元");
                if (result.getDelvyList().size() != 0) {
                    mTvFoodNextTime.setText(result.getDelvyList().get(0).getDelvystr() + "  " + result.getDelvyList().get(0).getDayOfWeek() + "  " + result.getDelvyList().get(0).getDlvtimestr());
                } else {
                    mTvFoodNextTime.setText("您暂时没有配送信息");
                }
                str = json;
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
