package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.JsonObject;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterMyBespeak;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMmyBespeak;
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
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import maxiaobu.mxbutilscodelibrary.NetworkUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 马小布 on 2016/12/27.
 * email：maxiaobu1216@gmail.com
 * 功能：我的预约
 * 伪码：
 * 待完成：
 */
public class MyBespeakActivity extends BaseAty implements OnRefreshListener, OnLoadMoreListener {
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
    @Bind(R.id.ivNoDataLogo)
    ImageView mIvNoDataLogo;
    @Bind(R.id.tv_nodata_content)
    TextView mTvNodataContent;
    @Bind(R.id.rlNoData)
    RelativeLayout mRlNoData;
    /**
     * 0刷新  1加载
     */
    private int mLoadType;
    private int mCurrentPage;
    private AdapterMyBespeak mAdapter;
    private List<BeanMmyBespeak.BespeaklistBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bespeak);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"我的预约");
        mLoadType = 0;
        mCurrentPage = 1;
        mData = new ArrayList<>();
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterMyBespeak(this, mData);
        mSwipeTarget.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        Observable<BeanMmyBespeak> objectObservable = App.getRetrofitUtil().getRetrofit()
                .getBeanMyBespeak(String.valueOf(mCurrentPage)
                ,  SPUtils.getString( Constant.MEMID));
        objectObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BeanMmyBespeak>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mSwipeToLoadLayout.setRefreshing(true);
                    }

                    @Override
                    public void onCompleted() {
                        if (mSwipeToLoadLayout != null) {
                            mSwipeToLoadLayout.setRefreshing(false);
                            mSwipeToLoadLayout.setLoadingMore(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mSwipeToLoadLayout != null) {
                            mSwipeToLoadLayout.setRefreshing(false);
                        }
                        new MaterialDialog.Builder(mActivity)
                                .title("网络加载失败")
                                .negativeColor(Color.parseColor("#Fb8435"))
                                .positiveColor(Color.parseColor("#Fb8435"))
                                .content("请检查网络设置")
                                .negativeText("刷新")
                                .positiveText("设置")
                                .onPositive((dialog, which) -> NetworkUtils.openWirelessSettings(mActivity))
                                .onNegative((dialog,which)->{
                                    dialog.dismiss();
                                    initData();
                                }).show();
                    }

                    @Override
                    public void onNext(BeanMmyBespeak beanMmyBespeak) {
                        if (mLoadType == 0) {//刷新
                            if (beanMmyBespeak.getBespeaklist().size() == 0) {
                                mRlNoData.setVisibility(View.VISIBLE);
                                mTvNodataContent.setText("暂无订单");
                            }
                            mData.clear();
                            mData.addAll(beanMmyBespeak.getBespeaklist());
                            mAdapter.notifyDataSetChanged();
                            if (mSwipeToLoadLayout != null) {
                                mSwipeToLoadLayout.setRefreshing(false);
                            }
                        } else if (mLoadType == 1) {//加载更多
                            if (beanMmyBespeak.getBespeaklist().size()>0){
                                int position = mAdapter.getItemCount();
                                mData.addAll(beanMmyBespeak.getBespeaklist());
                                mAdapter.notifyItemRangeInserted(position, beanMmyBespeak.getBespeaklist().size());
                            }else {
                                mCurrentPage--;
                                Toast.makeText(mActivity, "没有更多预约了", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MyBespeakActivity.this, "刷新什么情况", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoadType = 0;
        initData();
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mLoadType = 0;
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.postDelayed(() -> initData(), 2);
        }
    }

    @Override
    public void onLoadMore() {
        mCurrentPage++;
        mLoadType = 1;
        mSwipeToLoadLayout.post(() -> initData());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
