package com.maxiaobu.healthclub.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterNearList;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanNearList;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 莫小婷 on 2016/12/9.
 * 发现---附近的人
 */
public class NearFragment extends BaseFrg implements OnRefreshListener, OnLoadMoreListener {

    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private AdapterNearList mAdapterNearList;
    private List<BeanNearList.CoachListBean> mList;
    private int currentPage;
    private int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discovery_near, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {

        /**
         * 0:刷新  1：加载
         */
        type = 0;
        currentPage = 1;
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList = new ArrayList<>();
        mAdapterNearList = new AdapterNearList(getActivity(),mList);
        mAdapterNearList.setOnClickItem(new AdapterNearList.OnClickItem() {
            @Override
            public void onClickItemListen(String memid, String memrole) {
                Intent intenta = new Intent(getActivity(), PersionalActivity.class);
                intenta.putExtra("tarid",memid);
                intenta.putExtra("memrole",memrole);
                startActivity(intenta);
            }
        });
        mSwipeTarget.setAdapter(mAdapterNearList);
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("memid", SPUtils.getString(Constant.MEMID));
        params.put("pageIndex",String.valueOf(currentPage));
        params.put("latitude",SPUtils.getString(Constant.LATITUDE));
        params.put("longitude",SPUtils.getString(Constant.LONGITUDE));
        params.put("sorttytpe","distance");
        params.put("gender","all");
        App.getRequestInstance().post(getActivity(), UrlPath.URL_COACHES_LIST,  params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                BeanNearList beanNearList = new Gson().fromJson(s,BeanNearList.class);
                //刷新
                if(type == 0){
                    mSwipeToLoadLayout.setRefreshing(false);
                    mList.clear();
                    mList.addAll(beanNearList.getCoachList());
                    mAdapterNearList.notifyDataSetChanged();

                    //加载
                }else if(type == 1){
                    mSwipeToLoadLayout.setLoadingMore(false);
                    if(beanNearList.getCoachList().size() != 0){
                        int pos = mAdapterNearList.getItemCount();
                        mList.addAll(beanNearList.getCoachList());
                        mAdapterNearList.notifyItemRangeInserted(pos,mList.size());
                    }else {
                        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        currentPage--;
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
}
