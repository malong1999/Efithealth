package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterMyBespeak;
import com.maxiaobu.healthclub.adapter.AdapterTeachingAppointment;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMcoachBespeak;
import com.maxiaobu.healthclub.common.beangson.BeanMmyBespeak;
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
/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：我的教学预约
 * 伪码：
 * 待完成：
 */
public class MineTeachingAppointmentActivity extends BaseAty implements OnRefreshListener, OnLoadMoreListener {
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
    private AdapterTeachingAppointment mAdapter;
    private List<BeanMcoachBespeak.CoachBespeaklistBean> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_teaching_appointment);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"我的教学预约");
        mLoadType = 0;
        mCurrentPage = 1;
        mData = new ArrayList<>();
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterTeachingAppointment(this, mData);
        mAdapter.setOnItemClickListener(new AdapterTeachingAppointment.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BeanMcoachBespeak.CoachBespeaklistBean bean) {
                if ("gclub".equals(bean.getOrdtype()) || "gcoach".equals(bean.getOrdtype())) {
                    App.getRequestInstance().post(mActivity, UrlPath.URL_MCONFIRMLESSION,
                            new RequestParams("lessonid", bean.getLessonid()), new RequestListener() {
                                @Override
                                public void requestSuccess(String json) {
                                    try {
                                        JSONObject object = new JSONObject(json);
                                        Object msgFlag = object.get("msgFlag");
                                        if ("1".equals(msgFlag.toString())) {
                                            Toast.makeText(mActivity, "确认上课成功", Toast.LENGTH_SHORT).show();
                                            initData();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(mActivity, "返回数据解析失败", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void requestError(VolleyError volleyError, String s) {

                                }

                                @Override
                                public void noInternet(VolleyError volleyError, String s) {

                                }

                            });

                } else {
                    Intent intent = new Intent(mActivity, DataEntryActivity.class);
                    intent.putExtra("corderlessonid", bean.getCorderlessonid());
                    mActivity.startActivityForResult(intent, Constant.RESULT_REQUEST_ONE);
                }
            }
        });
        mSwipeTarget.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("memid", SPUtils.getString( Constant.MEMID));
        params.put("pageIndex", String.valueOf(mCurrentPage));
        App.getRequestInstance().post(this, UrlPath.URL_MCOACHBESPEAK, BeanMcoachBespeak.class, params, new RequestJsonListener<BeanMcoachBespeak>() {
            @Override
            public void requestSuccess(BeanMcoachBespeak beanMcoachBespeak) {
                if (mLoadType == 0) {//刷新
                    if (beanMcoachBespeak.getCoachBespeaklist().size() == 0) {
                        mRlNoData.setVisibility(View.VISIBLE);
                        mTvNodataContent.setText("暂无订单");
                    }
                    mData.clear();
                    mData.addAll(beanMcoachBespeak.getCoachBespeaklist());
                    mAdapter.notifyDataSetChanged();
                    if (mSwipeToLoadLayout != null) {
                        mSwipeToLoadLayout.setRefreshing(false);
                    }
                } else if (mLoadType == 1) {//加载更多
                    if (beanMcoachBespeak.getCoachBespeaklist().size()>0){
                        int position = mAdapter.getItemCount();
                        mData.addAll(beanMcoachBespeak.getCoachBespeaklist());
                        mAdapter.notifyItemRangeInserted(position, beanMcoachBespeak.getCoachBespeaklist().size());
                    }else {
                        Toast.makeText(mActivity, "没有更多教学预约了", Toast.LENGTH_SHORT).show();
                        mCurrentPage--;
                    }
                    mSwipeToLoadLayout.setLoadingMore(false);
                } else {
                    Toast.makeText(MineTeachingAppointmentActivity.this, "刷新什么情况", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void requestError(VolleyError volleyError, String s) {

            }

            @Override
            public void noInternet(VolleyError volleyError, String s) {

            }

        } );
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mLoadType = 0;
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
        mCurrentPage++;
        mLoadType = 1;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoadType = 0;
        initData();

    }
}
