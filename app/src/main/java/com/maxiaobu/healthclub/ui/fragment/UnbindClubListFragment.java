package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterUnbindClubListfrg;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMunbindList;
import com.maxiaobu.healthclub.ui.activity.ClubDetailActivity;
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
import butterknife.OnClick;

/**
 * 未绑定俱乐部列表
 */
public class UnbindClubListFragment extends BaseFrg
        implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener {


    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @Bind(R.id.ivNoDataLogo)
    ImageView mIvNoDataLogo;
    @Bind(R.id.tv_nodata_content)
    TextView mTvNodataContent;
    @Bind(R.id.rlNoData)
    RelativeLayout mRlNoData;
    /**
     * 0刷新  1加载
     */
    private int mDataType;
    private int mCurrentPage;

    private AdapterUnbindClubListfrg mAdapter;
    private List<BeanMunbindList.UnbindListBean> mData;

    public UnbindClubListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unbind_club_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        mCurrentPage = 1;
        mDataType = 0;
        mData = new ArrayList<>();
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterUnbindClubListfrg(getActivity(), mData);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterUnbindClubListfrg.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String clubId) {
                Intent intent = new Intent(getActivity(),
                        ClubDetailActivity.class);
                intent.putExtra("tarid", clubId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("pageIndex", String.valueOf(mCurrentPage)); //当前页码
        params.put("memid", SPUtils.getString(Constant.MEMID));//当前用户id
        App.getRequestInstance().post(getActivity(), UrlPath.URL_MUNBINDLIST,
                BeanMunbindList.class, params, new RequestJsonListener<BeanMunbindList>() {
                    @Override
                    public void requestSuccess(BeanMunbindList beanMunbindList) {
                        if (mDataType == 0) {//刷新
                            if (beanMunbindList.getUnbindList().size()>0) {
                                mData.clear();
                                mData.addAll(beanMunbindList.getUnbindList());
                                mAdapter.notifyDataSetChanged();
                                if (mSwipeToLoadLayout != null) {
                                    mSwipeToLoadLayout.setRefreshing(false);
                                }
                            } else {
                                mRlNoData.setVisibility(View.VISIBLE);
                            }
                        } else if (mDataType == 1) {//加载更多
                            if (beanMunbindList.getUnbindList().size()>0){
                                int position = mAdapter.getItemCount();
                                mData.addAll(beanMunbindList.getUnbindList());
                                mAdapter.notifyItemRangeInserted(position, beanMunbindList.getUnbindList().size());
                            }else {
                                Toast.makeText(getActivity(), "没有更多未绑定的俱乐部了", Toast.LENGTH_SHORT).show();
                                mCurrentPage--;
                            }
                            mSwipeToLoadLayout.setLoadingMore(false);
                        } else {
                            Toast.makeText(getActivity(), "刷新什么情况", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void requestError(VolleyError volleyError, String s) {

                    }

                    @Override
                    public void noInternet(VolleyError volleyError, String s) {

                    }

                }
        );
    }

    @OnClick({})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onLoadMore() {
        mCurrentPage++;
        mDataType = 1;
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mDataType = 0;
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
