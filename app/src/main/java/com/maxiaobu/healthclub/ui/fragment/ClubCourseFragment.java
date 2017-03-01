package com.maxiaobu.healthclub.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterClubPcourseFrg;
import com.maxiaobu.healthclub.adapter.BeanClubCourse;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMbclub;
import com.maxiaobu.healthclub.ui.activity.ClubDetailActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.activity.WeekCourseActivity;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：俱乐部课程
 * 伪码：
 * 待完成：
 */
public class ClubCourseFragment extends BaseFrg {
    @Bind(R.id.iv_top)
    ImageView mIvTop;
    @Bind(R.id.tv_personal)
    TextView mTvPersonal;
    @Bind(R.id.rv_personal)
    RecyclerView mRvPersonal;
    @Bind(R.id.tv_group)
    TextView mTvGroup;
    @Bind(R.id.rv_group)
    RecyclerView mRvGroup;
    @Bind(R.id.scroll_view)
    NestedScrollView mScrollView;

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    private List<BeanClubCourse.PcourseListBean> mPcourseData;
    private AdapterClubPcourseFrg mPersonalAdapter;

    public static Fragment getInstance() {
        ClubCourseFragment instance = new ClubCourseFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club_course, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        mPcourseData = new ArrayList<>();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        mIvTop.getLayoutParams().height = (int) (width / 2.6);
        mRvPersonal.setHasFixedSize(true);
        mRvPersonal.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManagerPersonal = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManagerPersonal.setSmoothScrollbarEnabled(true);
        layoutManagerPersonal.setAutoMeasureEnabled(true);
        mRvPersonal.setLayoutManager(layoutManagerPersonal);
        mRvPersonal.setItemAnimator(new DefaultItemAnimator());
        mPersonalAdapter = new AdapterClubPcourseFrg(getActivity(), mPcourseData);
        mRvPersonal.setAdapter(mPersonalAdapter);
        mIvTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WeekCourseActivity.class));
            }
        });

        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int dy = scrollY - oldScrollY;
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    if (null != ((ClubDetailActivity) getActivity()).getFabMenu() && ((ClubDetailActivity) getActivity()).getFabMenu().getVisibility() == View.VISIBLE)
                        ((ClubDetailActivity) getActivity()).getFabMenu().hideMenuButton(true);
                    if (null != ((ClubDetailActivity) getActivity()).getFabRelease() && ((ClubDetailActivity) getActivity()).getFabRelease().getVisibility() != View.GONE)
                        ((ClubDetailActivity) getActivity()).getFabRelease().hide(true);
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    if (null != ((ClubDetailActivity) getActivity()).getFabMenu() && ((ClubDetailActivity) getActivity()).getFabMenu().getVisibility() == View.VISIBLE)
                        ((ClubDetailActivity) getActivity()).getFabMenu().showMenuButton(true);
                    if (null != ((ClubDetailActivity) getActivity()).getFabRelease() && ((ClubDetailActivity) getActivity()).getFabRelease().getVisibility() != View.GONE)
                        ((ClubDetailActivity) getActivity()).getFabRelease().show(true);
                    controlsVisible = true;
                    scrolledDistance = 0;
                }
                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }

            }
        });
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("pageIndex", "1");
        params.put("clubmemid", getActivity().getIntent().getStringExtra("tarid"));
        App.getRequestInstance().post(getActivity(), UrlPath.URL_CLUB_DETAIL,
                params, new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {
                        BeanClubCourse object = JsonUtils.object(s, BeanClubCourse.class);
                        mPcourseData.addAll(object.getPcourseList());
                        final String clubid = object.getClubInfo().getClubid();
                        mIvTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), WeekCourseActivity.class);
                                intent.putExtra("clubid", clubid);
                                startActivity(intent);
                            }
                        });
                        if (mPcourseData.size() == 0)
                            mTvPersonal.setVisibility(View.GONE);

                        mTvGroup.setVisibility(View.GONE);
                        mPersonalAdapter.notifyDataSetChanged();
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
}
