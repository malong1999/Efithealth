package com.maxiaobu.healthclub.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterGoodsPayAty;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsPayActivity extends BaseAty implements View.OnClickListener {


    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.btn_pay)
    TextView mBtnPay;
    @Bind(R.id.btn_buy)
    TextView mBtnBuy;
    private ArrayList<Object> mData;
    private AdapterGoodsPayAty mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_pay);
        ButterKnife.bind(this);

        initView();
        initData();
    }


    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"支付");

        mData = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterGoodsPayAty(this, mData);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void initData() {

    }

    @OnClick({})
    @Override
    public void onClick(View v) {

    }
}
