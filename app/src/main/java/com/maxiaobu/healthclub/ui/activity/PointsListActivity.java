package com.maxiaobu.healthclub.ui.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterNearList;
import com.maxiaobu.healthclub.adapter.AdapterPointsList;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanDynamicCommentDetail;
import com.maxiaobu.healthclub.common.beangson.BeanNearList;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.mxbutilscodelibrary.Utils;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by 莫小婷 on 2016/12/24.
 * 点赞列表
 */
public class PointsListActivity extends BaseAty implements OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private int currentPage;
    private int type;
    private AdapterPointsList mAdapterPointsList;
    private List<BeanDynamicCommentDetail.PostBean.LikelistBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "点赞列表");
        /**
         * 0:刷新  1：加载
         */
        type = 0;
        currentPage = 1;
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mList = new Gson().fromJson(getIntent().getStringExtra("PointsContent"),BeanDynamicCommentDetail.class).getPost().getLikelist();
        mAdapterPointsList = new AdapterPointsList(this,mList);
        mAdapterPointsList.setOnClickItem(new AdapterPointsList.OnClickItem() {
            @Override
            public void onClickItemListen(String memid, String memrole) {
                Intent intenta = new Intent(PointsListActivity.this, PersionalActivity.class);
                intenta.putExtra("tarid",memid);
                intenta.putExtra("memrole",memrole);
                startActivity(intenta);
            }
        });
//        mSwipeToLoadLayout.setEnabled(false);
        mSwipeTarget.setAdapter(mAdapterPointsList);
    }

    @Override
    public void initData() {
        if(mSwipeToLoadLayout != null && type == 0){
            mSwipeToLoadLayout.setRefreshing(false);
        }else if(mSwipeToLoadLayout != null && type == 1){
            mSwipeToLoadLayout.setLoadingMore(false);
            Toast.makeText(this, "已经没有更多数据了", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        type = 0;
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            }, 2);
        }
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        type = 1;
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
