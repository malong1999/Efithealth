package com.maxiaobu.healthclub.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterClubListAty;
import com.maxiaobu.healthclub.adapter.AdapterScanCardAty;
import com.maxiaobu.healthclub.common.beangson.BeanWallet;
import com.maxiaobu.volleykit.JsonUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：卡包
 * 伪码：
 * 待完成：
 */
public class ScanCardActivity extends BaseAty implements View.OnClickListener {
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.activity_scan_card)
    RelativeLayout mActivityScanCard;
    private BeanWallet mData;
    private AdapterScanCardAty mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        String s = getIntent().getStringExtra("data");
        mData = JsonUtils.object(s, BeanWallet.class);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterScanCardAty(this, mData);
        mRecyclerView.setAdapter(mAdapter);
        mIvBack.setOnClickListener(v -> mActivity.finish());
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.iv_back)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            default:
                break;
        }
    }


}
