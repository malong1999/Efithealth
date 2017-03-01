package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.StudentListAdapter;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMineStudent;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestJsonListener;
import com.maxiaobu.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：我的学员
 * 伪码：
 * 待完成：
 */
public class MyStudentActivity extends BaseAty implements OnLoadMoreListener, OnRefreshListener {

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

    private List<BeanMineStudent.CoachmemberlistBean> mList;
    private StudentListAdapter mAdapter;
    /**
     * 0刷新  1加载
     */
    private int dataType;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_student);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"我的学员");
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        currentPage = 1;
        dataType = 0;
        mAdapter = new StudentListAdapter(this);
        mAdapter.setList(mList);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mAdapter.setListener(new StudentListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String memrole, String memid) {
                Intent intent = new Intent(MyStudentActivity.this,PersionalActivity.class);
                intent.putExtra("memrole",memrole);
                intent.putExtra("tarid",memid);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("pageIndex",String.valueOf(currentPage));
        params.put("coachid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(UrlPath.URL_MINE_STUDENT, this, BeanMineStudent.class, params, new RequestJsonListener<BeanMineStudent>() {
            @Override
            public void requestSuccess(BeanMineStudent result) {
                if(dataType == 0){//刷新
                    mList.clear();
                    mList.addAll(result.getCoachmemberlist());
                    mAdapter.setList(mList);
                    mAdapter.notifyDataSetChanged();
                    if (mSwipeToLoadLayout != null) {
                        mSwipeToLoadLayout.setRefreshing(false);
                    }

                }else if(dataType == 1){
                    if(result != null){
                        mList.addAll(result.getCoachmemberlist());
                        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), result.getCoachmemberlist().size());
                    }
                    if (mSwipeToLoadLayout != null) {
                        mSwipeToLoadLayout.setLoadingMore(false);
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
}
