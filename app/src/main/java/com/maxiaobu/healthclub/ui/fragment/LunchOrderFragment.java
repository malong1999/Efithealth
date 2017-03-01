package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.gson.JsonObject;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterLunchOrderFrg;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanCorderList;
import com.maxiaobu.healthclub.common.beangson.BeanLunchOrderList;
import com.maxiaobu.healthclub.ui.activity.CateringDetailActivity;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.mqldialoglibrary.dialog.MQLLoadingFragment;
import maxiaobu.mqldialoglibrary.materialdialogs.core.DialogAction;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import maxiaobu.mxbutilscodelibrary.NetworkUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 马小布 on 2016/12/27.
 * email：maxiaobu1216@gmail.com
 * 功能：配餐订单列表
 * 伪码：
 * 待完成：
 */
public class LunchOrderFragment extends BaseFrg implements OnRefreshListener, OnLoadMoreListener {
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
    private AdapterLunchOrderFrg mAdapter;
    private List<BeanLunchOrderList.ForderListBean> mData;
    private MQLLoadingFragment mLoadingFragment;

    public LunchOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lunch_order, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void initView() {
        mLoadType = 0;
        mCurrentPage = 1;
        mData = new ArrayList<>();
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterLunchOrderFrg(getActivity(), mData, this);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnCancelItemClickListener(new AdapterLunchOrderFrg.OnCancelItemClickListener() {
            @Override
            public void onItemClick(View view, final String what) {
                new MaterialDialog.Builder(getActivity())
                        .title("确定删除订单？")
                        .positiveColor(getResources().getColor(R.color.colorTextPrimary))
                        .positiveText("确认")
                        .onPositive((dialog, which) -> {
                            dialog.dismiss();
                            //http://192.168.1.121:8080/efithealth/mcancelForder.do?ordno=FO-20160726-170
                            // {"msgFlag":"1","msgContent":"取消订单成功"}
                            Observable<JsonObject> objectObservable =
                                    App.getRetrofitUtil().getRetrofit().getJsonCancelForder(what);
                            objectObservable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<JsonObject>() {
                                        @Override
                                        public void onStart() {
                                            super.onStart();
                                            mLoadingFragment = MQLLoadingFragment.getInstance(6,
                                                    4.0f,
                                                    false,
                                                    false, false, false);
                                            mLoadingFragment.show(getActivity().getSupportFragmentManager(),"1");
                                        }

                                        @Override
                                        public void onCompleted() {
                                            if (mLoadingFragment!=null)
                                                mLoadingFragment.dismiss();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if (mLoadingFragment!=null)
                                                mLoadingFragment.dismiss();
                                            Toast.makeText(getActivity(), "接口变了，我告诉我凹", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onNext(JsonObject object) {
                                            Toast.makeText(getActivity(), object.get("msgContent").toString(), Toast.LENGTH_SHORT).show();
                                            mCurrentPage = 1;
                                            mLoadType = 0;
                                            initData();
                                        }
                                    });
                        })
                        .negativeColor(getResources().getColor(R.color.colorTextPrimary))
                        .negativeText("取消")
                        .onNegative((dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        mAdapter.setOnDelayItemClickListener((view, what) -> new MaterialDialog.Builder(getActivity())
                .title("确定延期？")
                .positiveColor(getResources().getColor(R.color.colorTextPrimary))
                .positiveText("确认")

                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    //http://192.168.1.121:8080/efithealth/mextension.do?ordno=FO-20160726-170
                    //{"msgFlag":"1","msgContent":""}
                    //msgFlag：1成功了 0失败了
                    Observable<JsonObject> objectObservable =
                            App.getRetrofitUtil().getRetrofit().getJsonExtension(what);
                    objectObservable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<JsonObject>() {
                                @Override
                                public void onStart() {
                                    super.onStart();
                                    mLoadingFragment = MQLLoadingFragment.getInstance(6,
                                            4.0f,
                                            false,
                                            false, false, false);
                                    mLoadingFragment.show(getActivity().getSupportFragmentManager(),"1");
                                }

                                @Override
                                public void onCompleted() {
                                    if (mLoadingFragment!=null)
                                        mLoadingFragment.dismiss();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (mLoadingFragment!=null)
                                        mLoadingFragment.dismiss();
                                    Toast.makeText(getActivity(), "接口变了，我告诉我凹", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(JsonObject object) {
                                    Toast.makeText(getActivity(), object.get("msgContent").toString(), Toast.LENGTH_SHORT).show();
                                    mCurrentPage = 1;
                                    mLoadType = 0;
                                    initData();
                                }
                            });
                })
                .negativeColor(getResources().getColor(R.color.colorTextPrimary))
                .negativeText("取消")
                .onNegative((dialog, which) -> dialog.dismiss())
                .show());

        mAdapter.setOnAgainItemClickListener((view, what) -> {
            Intent intent = new Intent(getActivity(),
                    CateringDetailActivity.class);
            intent.putExtra("merid", what);
            startActivity(intent);
        });
    }

    @Override
    public void initData() {
        Observable<BeanLunchOrderList> corderListObservable = App.getRetrofitUtil()
                .getRetrofit().getBeanLunchOrderList("forderlist",
                        SPUtils.getString( Constant.MEMID), String.valueOf(mCurrentPage));
        corderListObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<BeanLunchOrderList>() {
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
                new MaterialDialog.Builder(getActivity())
                        .title("网络加载失败")
                        .negativeColor(Color.parseColor("#Fb8435"))
                        .positiveColor(Color.parseColor("#Fb8435"))
                        .content("请检查网络设置")
                        .negativeText("刷新")
                        .positiveText("设置")
                        .onPositive((dialog, which) -> NetworkUtils.openWirelessSettings(getActivity()))
                        .onNegative((dialog,which)->{
                            dialog.dismiss();
                            initData();
                        }).show();
            }

            @Override
            public void onNext(BeanLunchOrderList object) {
                if (mLoadType == 0) {//刷新
                    if (object.getForderList().size()==0){
                        mRlNoData.setVisibility(View.VISIBLE);
                        mTvNodataContent.setText("暂无订单");
                    }
                    mData.clear();
                    mData.addAll(object.getForderList());
                    mAdapter.notifyDataSetChanged();
                    if (mSwipeToLoadLayout != null) {
                        mSwipeToLoadLayout.setRefreshing(false);
                    }
                } else if (mLoadType == 1) {//加载更多
                    if (object.getForderList().size() > 0) {
                        int position = mAdapter.getItemCount();
                        mData.addAll(object.getForderList());
                        mAdapter.notifyItemRangeInserted(position, object.getForderList().size());
                    }else {
                        Toast.makeText(getActivity(), "没有更多订单了", Toast.LENGTH_SHORT).show();
                        mCurrentPage--;
                    }
                    mSwipeToLoadLayout.setLoadingMore(false);
                } else {
                    Toast.makeText(getActivity(), "刷新什么情况", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == 2)
            initData();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
