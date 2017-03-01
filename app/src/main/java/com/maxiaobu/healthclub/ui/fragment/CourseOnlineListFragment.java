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
import com.maxiaobu.healthclub.adapter.AdapterCoachesHistoryListfrg;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMcourseList;
import com.maxiaobu.healthclub.ui.activity.CoachesOnlineManageActivity;
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
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：上线课程列表
 * 伪码：
 * 待完成：
 */
public class CourseOnlineListFragment extends BaseFrg
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

    private List<BeanMcourseList.CourseListBean> mData;
    private AdapterCoachesHistoryListfrg mAdapter;

    public CourseOnlineListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_online_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
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
        mAdapter = new AdapterCoachesHistoryListfrg(getActivity(), mData);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterCoachesHistoryListfrg.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BeanMcourseList.CourseListBean courseListBean) {
                Intent intent = new Intent(getActivity(),
                        CoachesOnlineManageActivity.class);
                intent.putExtra("coachid", SPUtils.getString(Constant.MEMID));
                intent.putExtra("pcoursename", courseListBean.getPcoursename());// 课程名称
                intent.putExtra("pcoursecode", "");// 课程特征码
                intent.putExtra("imgsfile", courseListBean.getImgpfile());
                intent.putExtra("pcoursetimes", courseListBean.getPcoursetimes());
                intent.putExtra("pcoursedays", courseListBean.getPcoursedays());
                intent.putExtra("pcourseprice", courseListBean.getPcourseprice());
                intent.putExtra("resinform", courseListBean.getResinform());//内容
                intent.putExtra("clubid", courseListBean.getClubid());
                intent.putExtra("pcourseid", courseListBean.getPcourseid());
                startActivityForResult(intent,Constant.RESULT_REQUEST_SECOND);
            }
        });
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("linestatus", "1");
        params.put("coachid",SPUtils.getString(Constant.MEMID) );//当前用户id
        params.put("pageIndex", String.valueOf(mCurrentPage));//当前页数
        App.getRequestInstance().post(getActivity(), UrlPath.URL_MCOURSELIST,
                BeanMcourseList.class, params, new RequestJsonListener<BeanMcourseList>() {
            @Override
            public void requestSuccess(BeanMcourseList beanMcourseList) {
                if (mDataType == 0) {//刷新
                    if (beanMcourseList.getCourseList().size()>0) {
                        mData.clear();
                        mData.addAll(beanMcourseList.getCourseList());
                        mAdapter.notifyDataSetChanged();
                        if (mSwipeToLoadLayout != null) {
                            mSwipeToLoadLayout.setRefreshing(false);
                        }
                    } else {
                        mRlNoData.setVisibility(View.VISIBLE);
                    }
                } else if (mDataType == 1) {//加载更多
                    if (beanMcourseList.getCourseList().size()>0){
                        int position = mAdapter.getItemCount();
                        mData.addAll(beanMcourseList.getCourseList());
                        mAdapter.notifyItemRangeInserted(position, beanMcourseList.getCourseList().size());
                    }else {
                        Toast.makeText(getActivity(), "没有更多上线课程了", Toast.LENGTH_SHORT).show();
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

        });
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==Constant.RESULT_REQUEST_SECOND&&resultCode==Constant.RESULT_OK){
//            initData();
//        }
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
