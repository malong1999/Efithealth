package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsInfoActivity extends BaseAty implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.bind(this);
        initView();
        initData();

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
@OnClick({R.id.btn_buy})
    @Override
    public void onClick(View v) {
    switch (v.getId()) {
        case R.id.btn_buy:
            startActivity(new Intent(this,GoodsPayActivity.class));

            break;
        default:
            break;
    }
    }
}
