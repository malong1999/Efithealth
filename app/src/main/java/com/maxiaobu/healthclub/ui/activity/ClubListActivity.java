package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterClubListAty;
import com.maxiaobu.healthclub.adapter.AdapterCoachesListAty;
import com.maxiaobu.healthclub.adapter.RvCoachSelectAdapter;
import com.maxiaobu.healthclub.adapter.RvLunchSelectAdapter;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanClubList;
import com.maxiaobu.healthclub.common.beangson.BeanCoachesListAty;
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
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：俱乐部列表
 * 伪码：
 * 待完成：
 */
public class ClubListActivity extends BaseAty implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.tv_menu_time)
    TextView mTvMenuTime;
    @Bind(R.id.iv_menu_time)
    ImageView mIvMenuTime;
    @Bind(R.id.ry_select)
    LinearLayout mRySelect;
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @Bind(R.id.rv_select)
    RecyclerView mRvSelect;
    @Bind(R.id.fl_select)
    FrameLayout mFlSelect;
    @Bind(R.id.ly_select_root)
    LinearLayout mLySelectRoot;

    /**
     * 0刷新  1加载
     */
    private int mDataType;
    private int mCurrentPage;
    private int[] menuIcons;
    private String[] menuTitles;
    /**
     * 0隐藏；1排序
     */
    private int mMenuType;
    /**
     * 请求参数：排序
     * sorttype: 按好评(evascore)、按热度(coursetimes), 不排序不传值
     */
    private String mSortType;

    List<BeanClubList.ClubListBean>  mData;
    private AdapterClubListAty mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"club");
        mCurrentPage = 1;
        mDataType = 0;
        mData = new ArrayList<>();
        menuIcons = new int[]{};
        menuTitles = new String[]{};
        mMenuType = 0;
        mSortType = "all";
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);


        mSwipeTarget.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterClubListAty(this, mData);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterClubListAty.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String tarid) {
                Intent intent = new Intent(ClubListActivity.this,
                        ClubDetailActivity.class);
                intent.putExtra("tarid", tarid);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("pageIndex", String.valueOf(mCurrentPage)); //当前页码
        params.put("memid", SPUtils.getString(Constant.MEMID));//当前用户id
        params.put("latitude", SPUtils.getString(Constant.LATITUDE));
        params.put("longitude", SPUtils.getString(Constant.LONGITUDE));
        params.put("sorttype", mSortType);
        App.getRequestInstance().post(this, UrlPath.URL_MBCLUBLIST, BeanClubList.class, params, new RequestJsonListener<BeanClubList>() {
            @Override
            public void requestSuccess(BeanClubList beanClubList) {
                if (mDataType == 0) {
                    //刷新
                    mData.clear();
                    mData.addAll( beanClubList.getClubList());
                    mAdapter.notifyDataSetChanged();
                    if (mSwipeToLoadLayout != null) {
                        mSwipeToLoadLayout.setRefreshing(false);
                    }
                } else if (mDataType == 1) {//加载更多
                    if (beanClubList.getClubList().size()!=0){
                        int position = mAdapter.getItemCount();
                        mData.addAll( beanClubList.getClubList());
                        mAdapter.notifyItemRangeInserted(position,  beanClubList.getClubList().size());
                    }else {
                        Toast.makeText(mActivity, "没有更多俱乐部了", Toast.LENGTH_SHORT).show();
                        mCurrentPage--;
                    }
                    mSwipeToLoadLayout.setLoadingMore(false);
                } else {
                    Toast.makeText(mActivity, "刷新什么情况", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void requestError(VolleyError volleyError, String s) {
                Log.d("ClubListActivity", s);
                Log.d("ClubListActivity", "volleyError:" + volleyError);
            }

            @Override
            public void noInternet(VolleyError volleyError, String s) {

            }
        });
    }

    @OnClick({R.id.ry_select, R.id.fl_select})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //筛选
            case R.id.ry_select:
                switchMenu(1);
                break;
            //筛选
            case R.id.fl_select:
                switchMenu(0);
                break;
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

    /**
     * 显示/隐藏筛选列表
     * @param whichMenuType 0隐藏   1时间  2类型
     */
    private void switchMenu(int whichMenuType) {
//        mFlSelect.getVisibility()
        if (whichMenuType == 0) {//隐藏
            mMenuType = whichMenuType;
            mRvSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_menu_out));
            mFlSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_mask_out));
            mFlSelect.setVisibility(View.GONE);
            mRySelect.setBackgroundResource(R.drawable.bg_catering_select);
//            mRlTypeSelect.setBackgroundResource(R.drawable.bg_catering_select);
            mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_default);

        } else if (whichMenuType == mMenuType) {
            mMenuType = 0;
            mRvSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_menu_out));
            mFlSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_mask_out));
            mFlSelect.setVisibility(View.GONE);
            mRySelect.setBackgroundResource(R.drawable.bg_catering_select);
            mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_default);
        } else {//显示
            mFlSelect.setVisibility(View.VISIBLE);
            mRvSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_menu_in));
            mFlSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_mask_in));
            LinearLayoutManager menuTimeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRvSelect.setLayoutManager(menuTimeLayoutManager);
            mRvSelect.setItemAnimator(new DefaultItemAnimator());
            mMenuType = whichMenuType;
            menuIcons = new int[]{R.mipmap.icon_juli, R.mipmap.icon_haoping, R.mipmap.icon_redu, R.mipmap.icon_buxian};
            menuTitles = new String[]{"距离", "好评", "热度", "不限"};
            RvLunchSelectAdapter mMenuAdapter = new RvLunchSelectAdapter(this, menuIcons, menuTitles);
            mMenuAdapter.setLunchSelectItemClickListener(new RvLunchSelectAdapter.LunchSelectItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    String[] s = new String[]{"evascore", "evascore", "coursetimes", "all"}; //sorttype: merprice(按价格排序)； createtime(按时间排序)； 不排序不传值
                    mSortType = s[postion];
                    mTvMenuTime.setText(menuTitles[postion]);
                    switchMenu(0);
                    initData();
                }
            });
            mRySelect.setBackgroundResource(R.drawable.bg_catering_select_focused);
            mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_select);
            mRvSelect.setAdapter(mMenuAdapter);
        }
    }

}
