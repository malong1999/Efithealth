package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterCoachesListAty;
import com.maxiaobu.healthclub.adapter.RvCoachSelectAdapter;
import com.maxiaobu.healthclub.adapter.RvLunchSelectAdapter;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.beangson.BeanCoachesListAty;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.HealthUtil;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import maxiaobu.mqldialoglibrary.materialdialogs.core.MaterialDialog;
import maxiaobu.mxbutilscodelibrary.NetworkUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.transition.explode;

/**
 * Created by 马小布 on 2016/12/21.
 * introduction: 真他娘的不知道说点啥
 * email:maxiaobu1999@163.com
 * 功能：教练列表
 * 伪码：
 * 待完成：
 */
public class CoachesListActivity extends BaseAty implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.tv_menu_time)
    TextView mTvMenuTime;
    @Bind(R.id.iv_menu_time)
    ImageView mIvMenuTime;
    @Bind(R.id.rl_time_select)
    RelativeLayout mRlTimeSelect;
    @Bind(R.id.tv_menu_type)
    TextView mTvMenuType;
    @Bind(R.id.iv_menu_type)
    ImageView mIvMenuType;
    @Bind(R.id.rl_type_select)
    RelativeLayout mRlTypeSelect;
    @Bind(R.id.ll_select)
    LinearLayout mLlSelect;
    @Bind(R.id.rv_select)
    RecyclerView mRvSelect;
    @Bind(R.id.fl_select)
    FrameLayout mFlSelect;
    @Bind(R.id.iv_detail)
    ImageView mIvDetail;
    @Bind(R.id.container)
    CoordinatorLayout mContainer;
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private int mCurrentPage;
    /**
     * 0刷新  1加载
     *
     */
    private int mDataType;

    private int[] menuIcons;
    private String[] menuTitles;
    /**
     * 0隐藏；1排序；2筛选
     */
    private int mMenuType;
    /**
     * 请求参数：排序
     * sorttype: 按好评(evascore)、按热度(coursetimes), 不排序不传值
     */
    private String mSortType;
    /**
     * 请求参数：筛选
     * 1男；2女；3不限
     */
    private String mFilterType;
    List<BeanCoachesListAty.CoachListBean> mData;
    private AdapterCoachesListAty mAdapter;
    public LocationClient mLocationClient;
    public MyLocationListener myListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches_list);
        ButterKnife.bind(this);
        initView();
        if (NetworkUtils.isConnected(mActivity)) {
            mSwipeToLoadLayout.setRefreshing(true);
            mLocationClient = new LocationClient(App.getInstance());
            myListener = new MyLocationListener();
            mLocationClient.registerLocationListener(myListener);
            mLocationClient.setLocOption(App.getInstance().getLocationOption());
            mLocationClient.start();
        } else {
            HealthUtil.showNONetworkDialog(mActivity);
        }
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "名师高徒");
        mCurrentPage = 1;
        mDataType = 0;
        mData = new ArrayList<>();
        menuIcons = new int[]{};
        menuTitles = new String[]{};
        mMenuType = 0;
        mSortType = "all";
        mFilterType = "all";
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AdapterCoachesListAty(this, mData);
        mSwipeTarget.setAdapter(mAdapter);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener((view, name, sign, tarid, picurl, sName, sSign) -> {
            ImageView imageView = (ImageView) view;
            Drawable drawable = imageView.getDrawable();
            Intent intent = new Intent(CoachesListActivity.this,
                    PersionalActivity.class);
            intent.putExtra("tarid", tarid);
            intent.putExtra("tag", true);
            intent.putExtra("picurl", picurl);
            intent.putExtra("name", sName);
            intent.putExtra("sign", sSign);
            App.getInstance().setDrawableHolder(drawable);


            Pair<View, String> imagePair = Pair.create(view, mActivity.getString(R.string.transition));
            Pair<View, String> namePair = Pair.create(name, mActivity.getString(R.string.transitiona));
            Pair<View, String> signPair = Pair.create(sign, mActivity.getString(R.string.transitionb));
            ActivityOptionsCompat compat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(CoachesListActivity.this,
                            imagePair,namePair,signPair);
            ActivityCompat.startActivity(CoachesListActivity.this, intent, compat.toBundle());

        });
    }

    @Override
    public void initData() {

    }

    public void initData(String latitude, String longitude) {
//        Log.d("CoachesListActivity", SPUtils.getString(Constant.LATITUDE).equals("") ? "0" : SPUtils.getString(Constant.LATITUDE));
        Observable<JsonObject> objectObservable = App.getRetrofitUtil().getRetrofit().getJsonCoachList(String.valueOf(mCurrentPage)
                , SPUtils.getString(Constant.MEMID), latitude, longitude, mSortType, mFilterType);
        objectObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
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
                                .onNegative((dialog, which) -> {
                                    dialog.dismiss();
                                    initData(SPUtils.getString(Constant.LATITUDE).equals("") ?
                                                    "0" : SPUtils.getString(Constant.LATITUDE),
                                            SPUtils.getString(Constant.LONGITUDE).equals("") ?
                                                    "0" : SPUtils.getString(Constant.LONGITUDE));
                                }).show();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Gson gson = new Gson();
                        BeanCoachesListAty object = gson.fromJson(jsonObject, BeanCoachesListAty.class);
                        if (mDataType == 0) {//刷新
                            mData.clear();
                            if (object.getCoachList() != null)
                                mData.addAll(object.getCoachList());
                            mAdapter.notifyDataSetChanged();
                        } else if (mDataType == 1) {//加载更多
                            mSwipeToLoadLayout.setLoadingMore(false);
                            if (object.getCoachList().size() != 0) {
                                int position = mAdapter.getItemCount();
                                mData.addAll(object.getCoachList());
                                mAdapter.notifyItemRangeInserted(position, object.getCoachList().size());
                            } else {
                                Toast.makeText(mActivity, "没有更多教练了", Toast.LENGTH_SHORT).show();
                                mCurrentPage--;
                            }
                        } else {
                            Toast.makeText(mActivity, "刷新什么情况", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @OnClick({R.id.rl_time_select, R.id.rl_type_select, R.id.fl_select})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_time_select:
                switchMenu(1);
                break;
            case R.id.rl_type_select:
                switchMenu(2);
                break;
            case R.id.fl_select:
                switchMenu(0);
                break;
            case R.id.ivNoDataBac:
                initData();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    @Override
    public void onBackPressed() {
        if (mIvDetail.getVisibility() == View.VISIBLE) {
            mIvDetail.performClick();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            }
            super.onBackPressed();
        }
    }

    /**
     * 显示/隐藏筛选列表
     *
     * @param whichMenuType 0隐藏   1时间  2类型
     */
    private void switchMenu(int whichMenuType) {
        if (whichMenuType == 0) {//隐藏
            mMenuType = whichMenuType;
            mRvSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_menu_out));
            mFlSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_mask_out));
            mFlSelect.setVisibility(View.GONE);
            mRlTimeSelect.setBackgroundResource(R.drawable.bg_catering_select);
            mRlTypeSelect.setBackgroundResource(R.drawable.bg_catering_select);
            mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_default);
            mIvMenuType.setImageResource(R.mipmap.ic_lunch_arrow_default);
        } else if (whichMenuType == mMenuType) {
            mMenuType = 0;
            mRvSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_menu_out));
            mFlSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_mask_out));
            mFlSelect.setVisibility(View.GONE);
            mRlTimeSelect.setBackgroundResource(R.drawable.bg_catering_select);
            mRlTypeSelect.setBackgroundResource(R.drawable.bg_catering_select);
            mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_default);
            mIvMenuType.setImageResource(R.mipmap.ic_lunch_arrow_default);
        } else {//显示
            mFlSelect.setVisibility(View.VISIBLE);
            mRvSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_menu_in));
            mFlSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_mask_in));
            LinearLayoutManager menuTimeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRvSelect.setLayoutManager(menuTimeLayoutManager);
            mRvSelect.setItemAnimator(new DefaultItemAnimator());
            if (whichMenuType == 1) {//排序
                mMenuType = whichMenuType;
                menuIcons = new int[]{R.mipmap.icon_juli, R.mipmap.icon_haoping, R.mipmap.icon_redu, R.mipmap.icon_buxian};
                menuTitles = new String[]{"距离", "好评", "热度", "不限"};
                RvLunchSelectAdapter mMenuAdapter = new RvLunchSelectAdapter(this, menuIcons, menuTitles);
                mMenuAdapter.setLunchSelectItemClickListener(new RvLunchSelectAdapter.LunchSelectItemClickListener() {
                    @Override
                    public void onItemClick(View view, int postion) {
                        String[] s = new String[]{"distance", "evascore", "coursetimes", "all"}; //sorttype: merprice(按价格排序)； createtime(按时间排序)； 不排序不传值
                        mSortType = s[postion];
                        mTvMenuTime.setText(menuTitles[postion]);
                        switchMenu(0);
                        mDataType = 0;
                        mCurrentPage = 1;
                        initData();
                    }
                });
                mRvSelect.setAdapter(mMenuAdapter);
                mRlTimeSelect.setBackgroundResource(R.drawable.bg_catering_select_focused);
                mRlTypeSelect.setBackgroundResource(R.drawable.bg_catering_select);
                mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_select);
                mIvMenuType.setImageResource(R.mipmap.ic_lunch_arrow_default);
            } else {//筛选
                mMenuType = whichMenuType;
                menuTitles = new String[]{"男士", "女士", "不限"};
                menuIcons = new int[]{R.mipmap.icon_nanshi, R.mipmap.icon_nvshi, R.mipmap.icon_buxian};
                RvCoachSelectAdapter mMenuAdapter = new RvCoachSelectAdapter(this, menuIcons, menuTitles);
                mMenuAdapter.setLunchSelectItemClickListener((view, postion) -> {
                    String[] s = new String[]{"1", "0", "all"};//mertype:all全部；1增肌；2塑形；3减脂
                    mFilterType = s[postion];
                    mTvMenuType.setText(menuTitles[postion]);
                    switchMenu(0);
                    mDataType = 0;
                    mCurrentPage = 1;
                    initData();
                });
                mRvSelect.setAdapter(mMenuAdapter);
                mRlTypeSelect.setBackgroundResource(R.drawable.bg_catering_select_focused);
                mRlTimeSelect.setBackgroundResource(R.drawable.bg_catering_select);
                mIvMenuType.setImageResource(R.mipmap.ic_lunch_arrow_select);
                mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_default);
            }
        }
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mDataType = 0;
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.postDelayed(() -> initData(SPUtils.getString(Constant.LATITUDE).equals("") ?
                            "0" : SPUtils.getString(Constant.LATITUDE),
                    SPUtils.getString(Constant.LONGITUDE).equals("") ?
                            "0" : SPUtils.getString(Constant.LONGITUDE)), 2);
        }
    }

    @Override
    public void onLoadMore() {
        mCurrentPage++;
        mDataType = 1;
        mSwipeToLoadLayout.post(() -> initData(SPUtils.getString(Constant.LATITUDE).equals("") ?
                        "0" : SPUtils.getString(Constant.LATITUDE),
                SPUtils.getString(Constant.LONGITUDE).equals("") ?
                        "0" : SPUtils.getString(Constant.LONGITUDE)));
    }

    /**
     * Description:百度MAP 定位成功回调接口方法
     *
     * @author Xushd
     * @since 2016年2月20日上午11:14:36
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            initData(location.getLatitude() + "", location.getLongitude() + "");
            SPUtils.putString(com.maxiaobu.healthclub.common.Constant.LATITUDE, location.getLatitude() + "");
            SPUtils.putString(com.maxiaobu.healthclub.common.Constant.LONGITUDE, location.getLongitude() + "");
            String local_city = location.getCity();
            if (local_city != null) {
                SPUtils.putString(com.maxiaobu.healthclub.common.Constant.CITY, location.getCity());
            } else {
                SPUtils.putString(com.maxiaobu.healthclub.common.Constant.CITY, "沈阳");
            }
            mLocationClient.stop();
        }

        public void onReceivePoi(BDLocation location) {

        }
    }

}
