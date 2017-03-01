package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterMyFans;
import com.maxiaobu.healthclub.adapter.AdapterOtherAttention;
import com.maxiaobu.healthclub.adapter.AdapterOtherFans;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.common.beangson.BeanMyFans;
import com.maxiaobu.healthclub.common.beangson.BeanOtherAttention;
import com.maxiaobu.healthclub.common.beangson.BeanOtherFans;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2017/2/20.
 * email：maxiaobu1216@gmail.com
 * 功能：粉丝列表
 * 伪码：
 * 待完成：
 */
public class MyFansActivity extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    ListView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @Bind(R.id.image_view)
    ImageView mImageView;
    @Bind(R.id.ly_no_conversation)
    RelativeLayout mLyNoConversation;
    @Bind(R.id.image_text)
    TextView mImageText;

    private int currentPage;
    /**
     * 0刷新  1加载
     */
    private int dataType;

    private EventBus mEventBus;

    private AdapterOtherFans mAdapter;
    private List<BeanOtherFans.FollowerlistBean> mBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "粉丝列表");
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        currentPage = 1;
        dataType = 0;
        mBean = new ArrayList<>();
        mEventBus = EventBus.getDefault();
        mAdapter = new AdapterOtherFans(mBean, this);
        mAdapter.setOnClickLisente((layout, textView, imageView, pos, memid,socialrel) -> {
            if(socialrel.equals("none")){
                setMutualInfo(UrlPath.URL_FOLLOW,memid, R.mipmap.ic_mutualed, "已关注", 0xff939393,R.drawable.bg_my_friend_defult,pos,layout,textView,imageView,socialrel);
            }else if(socialrel.equals("follower")){
                setMutualInfo(UrlPath.URL_FOLLOW,memid, R.mipmap.ic_mutual, "互相关注", 0xff939393, R.drawable.bg_my_friend_defult,pos,layout,textView,imageView,socialrel);
            }else if(socialrel.equals("follow")){
                setMutualInfo(UrlPath.URL_UNFOLLOW, memid,R.mipmap.ic_add, "关注",  Color.WHITE, R.drawable.bg_my_friend_press,pos,layout,textView,imageView,socialrel);
            }else {
                setMutualInfo(UrlPath.URL_UNFOLLOW, memid,R.mipmap.ic_mutualedd, "关注", Color.WHITE,R.drawable.bg_my_friend_press,pos,layout,textView,imageView,socialrel);
            }
        });
        mAdapter.setOnClickItem((pos, memid, memrole) -> {
            Intent intent = new Intent(MyFansActivity.this, PersionalActivity.class);
            intent.putExtra("tarid", memid);
            intent.putExtra("memrole", memrole);
            startActivity(intent);
        });
        mSwipeTarget.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("socialrel.memid", getIntent().getStringExtra("fans"));
        params.put("memid", SPUtils.getString(Constant.MEMID));
        params.put("pageIndex", String.valueOf(currentPage));
        App.getRequestInstance().post(UrlPath.URL_FANS_LIST, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                BeanOtherFans beanOtherFans = new Gson().fromJson(json, BeanOtherFans.class);
                if (dataType == 0) {
                    if (mSwipeToLoadLayout != null)
                        mSwipeToLoadLayout.setRefreshing(false);
                    mBean.clear();
                    if (beanOtherFans.getFollowerlist() != null && beanOtherFans.getFollowerlist().size() != 0) {
                        mLyNoConversation.setVisibility(View.GONE);
                        mBean.addAll(beanOtherFans.getFollowerlist());
                    } else {
                        mLyNoConversation.setVisibility(View.VISIBLE);
                        mImageText.setText("暂无关注信息");
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (mSwipeToLoadLayout != null)
                        mSwipeToLoadLayout.setLoadingMore(false);
                    if (beanOtherFans.getFollowerlist() != null && beanOtherFans.getFollowerlist().size() != 0) {
                        mBean.addAll(beanOtherFans.getFollowerlist());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MyFansActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MyFansActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void noInternet(VolleyError e, String error) {
                if (mSwipeToLoadLayout != null) {
                    mSwipeToLoadLayout.setRefreshing(false);
                    mSwipeToLoadLayout.setLoadingMore(false);
                }
                Toast.makeText(MyFansActivity.this, error, Toast.LENGTH_SHORT).show();
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

    public void setMutualInfo(String url, String memid, int ImageResouceId, String text, int TextColor, int BaseResouceId, int pos, LinearLayout layout, TextView textView, ImageView imageView,String socialrel) {
        RequestParams params = new RequestParams();
        params.put("followid", memid);
        params.put("followerid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(url, MyFansActivity.this, params, new RequestListener() {
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
                        if(socialrel.equals("none")){
                            mBean.get(pos).setSocialrel("follow");
                            if(getIntent().getStringExtra("fans").equals(SPUtils.getString(Constant.MEMID))){
                                BeanEventBas beanEventBas = new BeanEventBas();
                                beanEventBas.setAddAttention(true);
                                mEventBus.post(beanEventBas);
                            }
                        }else if(socialrel.equals("follower")){
                            mBean.get(pos).setSocialrel("bothway");
                            if(getIntent().getStringExtra("fans").equals(SPUtils.getString(Constant.MEMID))){
                                BeanEventBas beanEventBas = new BeanEventBas();
                                beanEventBas.setAddAttention(true);
                                mEventBus.post(beanEventBas);
                            }
                        }else if(socialrel.equals("follow")){
                            mBean.get(pos).setSocialrel("none");
                            if(getIntent().getStringExtra("fans").equals(SPUtils.getString(Constant.MEMID))){
                                BeanEventBas beanEventBas = new BeanEventBas();
                                beanEventBas.setCancleAttention(true);
                                mEventBus.post(beanEventBas);
                            }
                        }else {
                            mBean.get(pos).setSocialrel("follower");
                            if(getIntent().getStringExtra("fans").equals(SPUtils.getString(Constant.MEMID))){
                                BeanEventBas beanEventBas = new BeanEventBas();
                                beanEventBas.setCancleAttention(true);
                                mEventBus.post(beanEventBas);
                            }
                        }
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
}
