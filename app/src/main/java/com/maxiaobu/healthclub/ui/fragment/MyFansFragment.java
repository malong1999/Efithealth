package com.maxiaobu.healthclub.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.BuildConfig;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterMyFans;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMyFans;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by 马小布 on 2017/2/20.
 * introduction：我长得真他娘的磕碜，单身未娶，求包养
 * email：maxiaobu1216@gmail.com
 * 功能：我的粉丝
 * 伪码：
 * 待完成：
 */
public class MyFansFragment extends BaseFrg implements OnLoadMoreListener, OnRefreshListener {

    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    ListView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @Bind(R.id.ly_no_conversation)
    RelativeLayout mLyNoConversation;

    private int currentPage;
    /**
     * 0刷新  1加载
     */
    private int dataType;

    private AdapterMyFans mAdapter;
    private List<BeanMyFans.FollowerlistBean> mBean;
    /**
     * 我的粉丝标志
     */
    private HashMap<Integer, Boolean> mBooleen;

    /**
     * 相互粉丝的标志
     */
    private HashMap<Integer, Boolean> mBothBoolean;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_fans, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void initView() {
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        currentPage = 1;
        dataType = 0;
        mBooleen = new HashMap<>();
        mBothBoolean = new HashMap<>();
        mBean = new ArrayList<>();
        mAdapter = new AdapterMyFans(mBean, getContext());
        mAdapter.setBooleen(mBooleen);
        mAdapter.setBothBoolean(mBothBoolean);
        mAdapter.setOnClickLayout((layout, textView, imageView, pos, memid) -> {
            if (mBooleen.containsKey(pos)) {
                if (!mBooleen.get(pos)) {
                    setMutualInfo(UrlPath.URL_FOLLOW, memid, R.mipmap.ic_mutualed, "已关注", 0xff939393, R.drawable.bg_my_friend_defult, mBooleen, pos, true, layout, textView, imageView);
                } else {
                    setMutualInfo(UrlPath.URL_UNFOLLOW, memid, R.mipmap.ic_add, "关注", Color.WHITE, R.drawable.bg_my_friend_press, mBooleen, pos, false, layout, textView, imageView);
                }
            } else if (mBothBoolean.containsKey(pos)) {
                if (!mBothBoolean.get(pos)) {
                    setMutualInfo(UrlPath.URL_UNFOLLOW, memid, R.mipmap.ic_mutualedd, "关注", Color.WHITE, R.drawable.bg_my_friend_press, mBothBoolean, pos, true, layout, textView, imageView);
                } else {
                    setMutualInfo(UrlPath.URL_FOLLOW, memid, R.mipmap.ic_mutual, "相互关注", 0xff939393, R.drawable.bg_my_friend_defult, mBothBoolean, pos, false, layout, textView, imageView);
                }
            }

        });
        mAdapter.setOnClickItem((pos, memid, memrole) -> {
            Intent intent = new Intent(getActivity(), PersionalActivity.class);
            intent.putExtra("tarid", memid);
            intent.putExtra("memrole", memrole);
            startActivity(intent);
        });
        mSwipeTarget.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("socialrel.memid", SPUtils.getString(Constant.MEMID));
        params.put("memid", SPUtils.getString(Constant.MEMID));
        params.put("pageIndex", String.valueOf(currentPage));
        Log.d("MyFansFragment", UrlPath.URL_FANS_LIST + "?socialrel.memid=" + SPUtils.getString(Constant.MEMID) + "&memid=" + SPUtils.getString(Constant.MEMID) + "&pageIndex=" + String.valueOf(currentPage));
        App.getRequestInstance().post(UrlPath.URL_FANS_LIST, getActivity(), params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                BeanMyFans beanMyFans = new Gson().fromJson(json, BeanMyFans.class);
                if (dataType == 0) {
                    if (mSwipeToLoadLayout != null)
                        mSwipeToLoadLayout.setRefreshing(false);
                    mBooleen.clear();
                    mBothBoolean.clear();
                    mBean.clear();
//                    Log.d("MyFansFragment", "beanMyFans.getFollowerList().size():" + beanMyFans.getFollowerlist().size());
                    if (beanMyFans.getFollowerlist() != null && beanMyFans.getFollowerlist().size() != 0) {
                        mLyNoConversation.setVisibility(View.GONE);
                        for (int i = 0; i < beanMyFans.getFollowerlist().size(); i++) {
                            if (beanMyFans.getFollowerlist().get(i).getSocialrel().equals("bothway")) {
                                mBothBoolean.put(i, false);
                            } else {
                                mBooleen.put(i, false);
                            }
                        }
                        mBean.addAll(beanMyFans.getFollowerlist());
                    } else {
                        mLyNoConversation.setVisibility(View.VISIBLE);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (mSwipeToLoadLayout != null)
                        mSwipeToLoadLayout.setLoadingMore(false);
                    if (beanMyFans.getFollowerlist() != null && beanMyFans.getFollowerlist().size() != 0) {
                        int BothSize = mBothBoolean.size();
                        int size = mBooleen.size();
                        for (int i = 0; i < beanMyFans.getFollowerlist().size(); i++) {
                            if (beanMyFans.getFollowerlist().get(i).getSocialrel().equals("bothway")) {
                                mBothBoolean.put(BothSize + i, false);
                            } else {
                                mBooleen.put(size + i, false);
                            }
                        }
                        mBean.addAll(beanMyFans.getFollowerlist());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        currentPage--;
                    }
                }
            }

            @Override
            public void requestError(VolleyError e, String error) {
                if (mSwipeToLoadLayout != null) {
                    mSwipeToLoadLayout.setRefreshing(false);
                    mSwipeToLoadLayout.setLoadingMore(false);
                }
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void noInternet(VolleyError e, String error) {
                if (mSwipeToLoadLayout != null) {
                    mSwipeToLoadLayout.setRefreshing(false);
                    mSwipeToLoadLayout.setLoadingMore(false);
                }
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        dataType = 1;
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        dataType = 0;
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            }, 2);
        }
    }

    public void setMutualInfo(String url, String memid, int ImageResouceId, String text, int TextColor, int BaseResouceId, HashMap<Integer, Boolean> hashMap, int pos, Boolean falg, LinearLayout layout, TextView textView, ImageView imageView) {
        RequestParams params = new RequestParams();
        params.put("followid", memid);
        params.put("followerid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(url, getActivity(), params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    String msgFlag = object.getString("msgFlag");
//                    String msgContent = object.getString("msgContent");
//                    Toast.makeText(getActivity(), msgContent, Toast.LENGTH_SHORT).show();
                    if (msgFlag.equals("1")) {
                        imageView.setImageResource(ImageResouceId);
                        textView.setText(text);
                        textView.setTextColor(TextColor);
                        layout.setBackgroundResource(BaseResouceId);
                        hashMap.remove(pos);
                        hashMap.put(pos, falg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e, String error) {

            }

            @Override
            public void noInternet(VolleyError e, String error) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mSwipeToLoadLayout != null) {
                mSwipeToLoadLayout.setRefreshing(true);
            } else {
                initData();
            }

        }
    }
}
