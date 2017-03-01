package com.maxiaobu.healthclub.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.DistributionDetailListAdapter;
import com.maxiaobu.healthclub.common.beangson.BeanFoodOrderDetail;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：配送详情
 * 伪码：
 * 待完成：
 */
public class DistributionDetailActivity extends BaseAty {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.list_view)
    ListView mListView;

    private DistributionDetailListAdapter mAdapter;
    private BeanFoodOrderDetail mBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"配送详情");
        mAdapter = new DistributionDetailListAdapter(this);
        String str = getIntent().getStringExtra("str");
        mBean = new Gson().fromJson(str,BeanFoodOrderDetail.class);
        mAdapter.setList(mBean);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {

    }
}
