package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 莫玉婷 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：余额
 * 伪码：
 * 待完成：
 */
public class BalanceActivity extends BaseAty implements View.OnClickListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.iv_user_image)
    ImageView mIvUserImage;
    @Bind(R.id.tv_user_name)
    TextView mTvUserName;
    @Bind(R.id.tv_balance_num)
    TextView mTvBalanceNum;
    @Bind(R.id.rl_balance)
    RelativeLayout mRlBalance;
    @Bind(R.id.rl_recharge)
    RelativeLayout mRlRecharge;
    @Bind(R.id.rl_withdraw)
    RelativeLayout mRlWithdraw;
    @Bind(R.id.rl_detail)
    RelativeLayout mRlDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_balance);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "余额");
        mRlBalance.setOnClickListener(this);
        mRlRecharge.setOnClickListener(this);
        mRlWithdraw.setOnClickListener(this);
        mRlDetail.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Glide.with(this).load(getIntent().getStringExtra("userImage")).placeholder(R.mipmap.ic_place_holder).into(mIvUserImage);
        mTvUserName.setText(getIntent().getStringExtra("name"));
        mTvBalanceNum.setText(getIntent().getStringExtra("balance"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rl_detail})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_detail:
                Intent intent = new Intent(this,BalanceDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
