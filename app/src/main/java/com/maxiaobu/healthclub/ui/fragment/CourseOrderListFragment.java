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
import com.maxiaobu.healthclub.adapter.AdapterCourseOrderFrg;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanCorderList;
import com.maxiaobu.healthclub.listener.OnAgainItemClickListener;
import com.maxiaobu.healthclub.listener.OnCancelItemClickListener;
import com.maxiaobu.healthclub.listener.OnImageItemClickListener;
import com.maxiaobu.healthclub.ui.activity.CateringDetailActivity;
import com.maxiaobu.healthclub.ui.activity.GcourseActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalCourseActivity;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestJsonListener;
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

import static com.maxiaobu.healthclub.R.id.media_actions;
import static com.maxiaobu.healthclub.R.id.view;

/**
 * Created by 马小布 on 2016/12/27.
 * email：maxiaobu1216@gmail.com
 * 功能：课程订单列表
 * 伪码：
 * 待完成：
 */
public class CourseOrderListFragment extends BaseFrg implements OnRefreshListener, OnLoadMoreListener {
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
    private AdapterCourseOrderFrg mAdapter;
    private List<BeanCorderList.CorderListBean> mData;
    private MQLLoadingFragment mLoadingFragment;

    public CourseOrderListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_course_order_list, container, false);
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
        mAdapter = new AdapterCourseOrderFrg(getActivity(), mData);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnCancelItemClickListener((view13, what) -> new MaterialDialog.Builder(getActivity())
                .title("确定删除订单？")
                .positiveColor(getResources().getColor(R.color.colorTextPrimary))
                .positiveText("确认")
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    Observable<JsonObject> objectObservable = App.getRetrofitUtil().getRetrofit()
                            .getJsonDeleteByList("'" + what + "'", "corderlist");
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
                                }

                                @Override
                                public void onNext(JsonObject object) {
                                    SPUtils.putBoolean(Constant.FALG, true);
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
        mAdapter.setOnAgainItemClickListener((view12, listBean) -> {
            Intent intent = new Intent();
            if ("gclub".equals(listBean.getOrdtype()) || "gcoach".equals(listBean.getOrdtype())) {
                intent.setClass(getActivity(), GcourseActivity.class);
                String page = "gcourse.html?gcalid=" + listBean.getCourseid() + "&conphone=13400000000";
                intent.putExtra("page", page);
                getActivity().startActivity(intent);
            } else {
                intent.setClass(getActivity(), PersionalCourseActivity.class);
                intent.putExtra("pcourseid", listBean.getCourseid());
                startActivity(intent);

            }


        });
        mAdapter.setOnImageItemClickListener((view1, tarid) -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PersionalActivity.class);
            intent.putExtra("memrole", "coach");
            intent.putExtra("tarid", tarid);
            getActivity().startActivity(intent);
        });
    }

    @Override
    public void initData() {
        Observable<BeanCorderList> corderListObservable = App.getRetrofitUtil()
                .getRetrofit().getBeanCorderList(String.valueOf(mCurrentPage),
                        "corderlist", SPUtils.getString(Constant.MEMID));
        corderListObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BeanCorderList>() {
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
                    public void onNext(BeanCorderList result) {
                        if (mLoadType == 0) {//刷新
                            if (result.getCorderList().size() == 0) {
                                mRlNoData.setVisibility(View.VISIBLE);
                                mTvNodataContent.setText("暂无订单");
                            }
                            mData.clear();
                            mData.addAll(result.getCorderList());
                            mAdapter.notifyDataSetChanged();

                        } else if (mLoadType == 1) {//加载更多
                            if (result.getCorderList().size() > 0) {
                                int position = mAdapter.getItemCount();
                                mData.addAll(result.getCorderList());
                                mAdapter.notifyItemRangeInserted(position, result.getCorderList().size());
                            }else {
                                Toast.makeText(getActivity(), "没有更多订单了", Toast.LENGTH_SHORT).show();
                                mCurrentPage--;
                            }

                        } else {
                            Toast.makeText(getActivity(), "刷新什么情况", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        RequestParams params = new RequestParams();
//        params.put("pageIndex", String.valueOf(mCurrentPage));
//        params.put("listtype", "corderlist");
//        params.put("memid", SPUtils.getString(Constant.MEMID));
////        Log.d("++", UrlPath.URL_FOOD_ORDER_LIST+"?listtype=corderlist&memid="+SPUtils.getString( Constant.MEMID)+"&pageIndex=1");
//        App.getRequestInstance().post(getActivity(), UrlPath.URL_FOOD_ORDER_LIST,
//                BeanCorderList.class, params, new RequestJsonListener<BeanCorderList>() {
//            @Override
//            public void requestSuccess(BeanCorderList result) {
//                if (mLoadType == 0) {//刷新
//                    if (result.getCorderList().size() == 0) {
//                        mRlNoData.setVisibility(View.VISIBLE);
//                        mTvNodataContent.setText("暂无订单");
//                    }
//                    mData.clear();
//                    mData.addAll(result.getCorderList());
//                    mAdapter.notifyDataSetChanged();
//                    if (mSwipeToLoadLayout != null) {
//                        mSwipeToLoadLayout.setRefreshing(false);
//                    }
//                } else if (mLoadType == 1) {//加载更多
//                    if (result.getCorderList().size() > 0) {
//                        int position = mAdapter.getItemCount();
//                        mData.addAll(result.getCorderList());
//                        mAdapter.notifyItemRangeInserted(position, result.getCorderList().size());
//                    }else {
//                        Toast.makeText(getActivity(), "没有更多订单了", Toast.LENGTH_SHORT).show();
//                        mCurrentPage--;
//                    }
//                    mSwipeToLoadLayout.setLoadingMore(false);
//                } else {
//                    Toast.makeText(getActivity(), "刷新什么情况", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//                    @Override
//                    public void requestError(VolleyError volleyError, String s) {
//
//                    }
//
//                    @Override
//                    public void noInternet(VolleyError volleyError, String s) {
//
//                    }
//
//        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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

}
