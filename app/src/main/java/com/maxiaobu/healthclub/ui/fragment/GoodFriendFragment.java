package com.maxiaobu.healthclub.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterMyFri;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMyFriend;
import com.maxiaobu.healthclub.ui.activity.PersionalActivity;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import maxiaobu.mqldialoglibrary.dialog.MQLLoadingFragment;
/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：关注列表
 * 伪码：
 * 待完成：
 */
public class GoodFriendFragment extends BaseFrg implements OnRefreshListener, OnLoadMoreListener {
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

    private AdapterMyFri mAdapterMyFri;
    private List<BeanMyFriend.FriendListBean> mBeanMyFriend;

    private List<Boolean> mBooleen;

    private int currentPage;
    /**
     * 0刷新  1加载
     */
    private int dataType;

    private MQLLoadingFragment mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_firend, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void initView() {
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mBeanMyFriend = new ArrayList<>();
        currentPage = 1;
        dataType = 0;
        mBooleen = new ArrayList<>();
        mAdapterMyFri = new AdapterMyFri(mBeanMyFriend, getContext());
        mAdapterMyFri.setBoolean(mBooleen);
        mAdapterMyFri.setOnClickButton((layout, textView, imageView, pos, memid) -> {
            if (!mBooleen.get(pos)) {
                setMutualInfo(UrlPath.URL_UNFOLLOW, memid, R.mipmap.ic_mutualedd, "关注",
                        Color.WHITE, R.drawable.bg_my_friend_press, mBooleen, pos, true,
                        layout, textView, imageView);
            } else {
                setMutualInfo(UrlPath.URL_FOLLOW, memid, R.mipmap.ic_mutual, "互相关注",
                        0xff939393, R.drawable.bg_my_friend_defult, mBooleen, pos, false,
                        layout, textView, imageView);
            }
        });
        mAdapterMyFri.setOnClickItem((memid, memrole) -> {
            Intent intent = new Intent(getActivity(), PersionalActivity.class);
            intent.putExtra("tarid",memid);
            intent.putExtra("memrole",memrole);
            startActivity(intent);
        });
        mSwipeTarget.setAdapter(mAdapterMyFri);

    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("socialrel.memid", SPUtils.getString(Constant.MEMID));
        params.put("pageIndex", String.valueOf(currentPage));
        App.getRequestInstance().post( UrlPath.URL_FRIEND_LIST, getActivity(),
                params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                BeanMyFriend beanMyFriend = new Gson().fromJson(json, BeanMyFriend.class);
                if (dataType == 0) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    if (mSwipeToLoadLayout != null)
                        mSwipeToLoadLayout.setRefreshing(false);
                    mBooleen.clear();
                    mBeanMyFriend.clear();
                    if(beanMyFriend.getFriendList() != null && beanMyFriend.getFriendList().size() != 0){
                        mLyNoConversation.setVisibility(View.GONE);
                        for (int i = 0; i < beanMyFriend.getFriendList().size(); i++) {
                            mBooleen.add(false);
                        }
                        mBeanMyFriend.addAll(beanMyFriend.getFriendList());
                    }else {
                        mLyNoConversation.setVisibility(View.VISIBLE);
                    }
                    mAdapterMyFri.notifyDataSetChanged();
                } else {
                    if (mSwipeToLoadLayout != null)
                        mSwipeToLoadLayout.setLoadingMore(false);
                    if (beanMyFriend.getFriendList() != null && beanMyFriend.getFriendList().size() != 0) {
                        for (int i = 0; i < beanMyFriend.getFriendList().size(); i++) {
                            mBooleen.add(false);
                        }
                        mBeanMyFriend.addAll(beanMyFriend.getFriendList());
                        mAdapterMyFri.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        currentPage -- ;
                    }
                }
            }

            @Override
            public void requestError(VolleyError e, String error) {
                if (mDialog != null)
                    mDialog.dismiss();
                if (mSwipeToLoadLayout != null){
                    mSwipeToLoadLayout.setRefreshing(false);
                    mSwipeToLoadLayout.setLoadingMore(false);
                }
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void noInternet(VolleyError e, String error) {
                if (mDialog != null)
                    mDialog.dismiss();
                if (mSwipeToLoadLayout != null){
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

    public void setMutualInfo(String url, String memid, int ImageResouceId, String text, int TextColor, int BaseResouceId, List<Boolean> list, int pos, Boolean falg, LinearLayout layout, TextView textView, ImageView imageView) {
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
                        list.remove(pos);
                        list.add(pos, falg);
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
        if(isVisibleToUser){
            if (mSwipeToLoadLayout != null){
                mSwipeToLoadLayout.setRefreshing(true);
            }else {
                mDialog = MQLLoadingFragment.getInstance(6, 4.0f, false, false, false, false);
                mDialog.show(getFragmentManager(), "");
                initData();
            }

        }
    }
}
