package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterGcourseFrg;
import com.maxiaobu.healthclub.adapter.AdapterPcourseFrg;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanCoachesDetail;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.activity.PersionalCourseActivity;
import com.maxiaobu.volleykit.JsonUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.type;

/**
 * Created by 马小布 on 2017/1/21.
 * email：maxiaobu1216@gmail.com
 * 功能：教练个人页课程列表
 * 伪码：
 * 待完成：
 */
public class CoachCourseFragment extends BaseFrg {


    @Bind(R.id.ll_layout)
    LinearLayout mLlLayout;
    @Bind(R.id.view_personal)
    View mViewPersonal;
    @Bind(R.id.tv_personal)
    TextView mTvPersonal;
    @Bind(R.id.rv_personal)
    RecyclerView mRvPersonal;
    @Bind(R.id.view_group)
    View mViewGroup;
    @Bind(R.id.tv_group)
    TextView mTvGroup;
    @Bind(R.id.rv_group)
    RecyclerView mRvGroup;
    @Bind(R.id.nest_scroll_view)
    NestedScrollView mNestScrollView;
    private List<BeanCoachesDetail.PcourseListBean> mPcourseData;
    private List<BeanCoachesDetail.GcourseListBean> mGcourseData;
    private AdapterPcourseFrg mPersonalAdapter;
    private AdapterGcourseFrg mGroupAdapter;

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    public static Fragment getInstance() {
        CoachCourseFragment instance = new CoachCourseFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_course, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    @Override
    public void initView() {
        mPcourseData = new ArrayList<>();
        mGcourseData = new ArrayList<>();
        mRvPersonal.setHasFixedSize(true);
        mRvPersonal.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManagerPersonal = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManagerPersonal.setSmoothScrollbarEnabled(true);
        layoutManagerPersonal.setAutoMeasureEnabled(true);
        mRvPersonal.setLayoutManager(layoutManagerPersonal);
        mRvPersonal.setItemAnimator(new DefaultItemAnimator());
        mPersonalAdapter = new AdapterPcourseFrg(getActivity(), mPcourseData);
        mRvPersonal.setAdapter(mPersonalAdapter);
        mPersonalAdapter.setOnMqlClickListener((view, position, string) -> {
            Intent intent = new Intent(getActivity(), PersionalCourseActivity.class);
            intent.putExtra("pcourseid", string);
            getActivity().startActivity(intent);
        });


        mRvGroup.setHasFixedSize(true);
        mRvGroup.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManagerGroup = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManagerGroup.setSmoothScrollbarEnabled(true);
        layoutManagerGroup.setAutoMeasureEnabled(true);
        mRvGroup.setLayoutManager(layoutManagerGroup);
        mRvGroup.setItemAnimator(new DefaultItemAnimator());
        mGroupAdapter = new AdapterGcourseFrg(getActivity(), mGcourseData);
        mRvGroup.setAdapter(mGroupAdapter);

        mNestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int dy = scrollY - oldScrollY;
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    if (null != ((PersionalActivity) getActivity()).getFabMenu() && ((PersionalActivity) getActivity()).getFabMenu().getVisibility() == View.VISIBLE)
                        ((PersionalActivity) getActivity()).getFabMenu().hideMenuButton(true);
                    if (null != ((PersionalActivity) getActivity()).getFabRelease() && ((PersionalActivity) getActivity()).getFabRelease().getVisibility() != View.GONE)
                        ((PersionalActivity) getActivity()).getFabRelease().hide(true);
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    if (null != ((PersionalActivity) getActivity()).getFabMenu() && ((PersionalActivity) getActivity()).getFabMenu().getVisibility() == View.VISIBLE)
                        ((PersionalActivity) getActivity()).getFabMenu().showMenuButton(true);
                    if (null != ((PersionalActivity) getActivity()).getFabRelease() && ((PersionalActivity) getActivity()).getFabRelease().getVisibility() != View.GONE)
                        ((PersionalActivity) getActivity()).getFabRelease().show(true);
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
//        params.put("pageIndex", "1");
        params.put("tarid", getActivity().getIntent().getStringExtra("tarid"));
        App.getRequestInstance().post(UrlPath.URL_COACHES_DETAIL, getActivity(),
                params, new RequestListener() {
                    @Override
                    public void requestSuccess(String s) {
                        Log.d("CoachCourseFragment", s);
                        BeanCoachesDetail object = JsonUtils.object(s, BeanCoachesDetail.class);
                        mPcourseData.addAll(object.getPcourseList());
                        if (object.getGcourseList() != null)
                            mGcourseData.addAll(object.getGcourseList());
                        if (mPcourseData.size() == 0) {
                            mTvPersonal.setVisibility(View.GONE);
                            mViewPersonal.setVisibility(View.GONE);
                        }
                        if (mGcourseData.size() == 0) {
                            mTvGroup.setVisibility(View.GONE);
                            mViewGroup.setVisibility(View.GONE);
                        }
                        mPersonalAdapter.notifyDataSetChanged();
                        mGroupAdapter.notifyDataSetChanged();
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
