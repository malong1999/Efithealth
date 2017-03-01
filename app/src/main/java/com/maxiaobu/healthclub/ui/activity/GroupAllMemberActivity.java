package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterGroupAllMember;
import com.maxiaobu.healthclub.common.beangson.BeanGroupDetails;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by 马小布 on 2017/1/19.
 * introduction：我长得真他娘的磕碜，单身未娶，求包养
 * email：maxiaobu1216@gmail.com
 * 功能：群成员
 * 伪码：
 * 待完成：
 */
public class GroupAllMemberActivity extends BaseAty implements OnLoadMoreListener, OnRefreshListener, AdapterGroupAllMember.OnItemClick {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;


    /**
     * 0刷新  1加载
     */
    private int dataType;
    /**
     * 当前页数
     */
    private int currentPage;

    private List<BeanGroupDetails.GroupBean.MemListBean> mListBeen;
    private AdapterGroupAllMember mAdapterGroupAllMember;
    private BeanGroupDetails mBeanGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_all_member);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "群成员");
        if (getIntent().getStringExtra("group_member_str") != null) {
            mBeanGroupList = new Gson().fromJson(getIntent().getStringExtra("group_member_str"), BeanGroupDetails.class);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        currentPage = 1;
        dataType = 0;
        mListBeen = new ArrayList<>();
        mAdapterGroupAllMember = new AdapterGroupAllMember(this, mListBeen);
        mAdapterGroupAllMember.setOnItemClick(this);
        mRecyclerView.setAdapter(mAdapterGroupAllMember);
    }

    @Override
    public void initData() {


        if (dataType == 1) {
            mSwipeToLoadLayout.setLoadingMore(false);
            Toast.makeText(this, "没有更多的数据", Toast.LENGTH_SHORT).show();
        } else {

            mSwipeToLoadLayout.setRefreshing(false);
            mListBeen.clear();
            if (mBeanGroupList != null){
                mListBeen.addAll(mBeanGroupList.getGroup().getMemList());
            }else {
                Toast.makeText(this, "获取不到数据", Toast.LENGTH_SHORT).show();
            }

            mAdapterGroupAllMember.notifyDataSetChanged();

        }

    }

    @Override
    public void onLoadMore() {
        currentPage++;
        dataType = 1;
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        dataType = 0;
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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClickListen(String memid, int pos, String memrole) {

        Intent intent = new Intent();
        intent.putExtra("memrole",memrole);
        intent.putExtra("tarid",memid);
        if (memrole.equals("mem") || memrole.equals("coach")){
            intent.setClass(this,PersionalActivity.class);
        }else {
            intent.setClass(this,ClubDetailActivity.class);
        }

        startActivity(intent);

    }
}
