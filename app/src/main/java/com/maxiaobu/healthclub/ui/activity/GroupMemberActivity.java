package com.maxiaobu.healthclub.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.AdapterGroupMemberManage;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanEventBas;
import com.maxiaobu.healthclub.common.beangson.BeanGroupDetails;
import com.maxiaobu.healthclub.ui.weiget.dialog.CustomDialog;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：群成员管理
 * 伪码：
 * 待完成：
 */
public class GroupMemberActivity extends BaseAty implements OnRefreshListener, OnLoadMoreListener, AdapterGroupMemberManage.OnRemoveClick {
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
    private int mCurrentPage;
    /**
     * 0刷新  1加载
     */
    private int mDataType;
    private AdapterGroupMemberManage mAdapterGroupMemberManage;
    private List<BeanGroupDetails.GroupBean.MemListBean> mListBeen;
    private BeanGroupDetails mBeanGroupList;
    private CustomDialog mDialog;
    private EventBus mEventBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "群成员管理(" + getIntent().getStringExtra("groupSize") + ")");
        mBeanGroupList = new Gson().fromJson(getIntent().getStringExtra("groupMember"), BeanGroupDetails.class);
        mCurrentPage = 1;
        mDataType = 0;
        mEventBus = EventBus.getDefault();
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mSwipeToLoadLayout.setRefreshing(true);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mListBeen = new ArrayList<>();
        mAdapterGroupMemberManage = new AdapterGroupMemberManage(this);
        mAdapterGroupMemberManage.setListBeen(mListBeen);
        mAdapterGroupMemberManage.setManageMemid(mBeanGroupList.getGroup().getManagerid());
        mSwipeTarget.setAdapter(mAdapterGroupMemberManage);
        mAdapterGroupMemberManage.setOnRemoveClick(this);
    }

    @Override
    public void initData() {
        if (mDataType == 1) {
            mSwipeToLoadLayout.setLoadingMore(false);
            Toast.makeText(this, "没有更多的数据", Toast.LENGTH_SHORT).show();
        } else {
            mSwipeToLoadLayout.setRefreshing(false);
            mListBeen.clear();
            mListBeen.addAll(mBeanGroupList.getGroup().getMemList());
            mAdapterGroupMemberManage.notifyDataSetChanged();
        }
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
    public void onRemoveMenber(View view,String memid, int pos) {
        mDialog = new CustomDialog(this, new CustomDialog.SetOnClick() {
            @Override
            public void onPosition(View view, AlertDialog dialog) {
                removeMem(memid,pos);
            }

            @Override
            public void onCancle(View view, AlertDialog dialog) {

            }
        });
        mDialog.setTitle("移除用户");
        mDialog.setContent("您确定要移除该用户吗？");
        mDialog.setTitleColor(Color.RED);
        mDialog.create();
        mDialog.show();
    }

    public void removeMem(String memid, int pos){
        RequestParams params = new RequestParams();
        params.put("groupmember.memid", memid);
        params.put("groupmember.groupid", getIntent().getStringExtra("groupIid"));
        App.getRequestInstance().post(UrlPath.URL_TEXT, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String msgFlag = object.getString("msgFlag");
                    String msgContent = object.getString("msgContent");
                    if (msgFlag.equals("1")) {
                        Toast.makeText(GroupMemberActivity.this, "移除成功", Toast.LENGTH_SHORT).show();

                        new Thread(() -> {
                            try {
                                EMClient.getInstance().groupManager().removeUserFromGroup(getIntent().getStringExtra("id"), memid.toLowerCase());
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }).start();

                        //刷新
                        mSwipeToLoadLayout.setRefreshing(true);
                        mSwipeToLoadLayout.setRefreshing(false);
                        mListBeen.clear();
                        mListBeen.addAll(mBeanGroupList.getGroup().getMemList());
                        mListBeen.remove(pos);
                        mAdapterGroupMemberManage.notifyDataSetChanged();
                        BeanEventBas beanEventBas = new BeanEventBas();
                        beanEventBas.setRefresh(true);
                        mEventBus.post(beanEventBas);

                    } else {
                        Toast.makeText(GroupMemberActivity.this, msgContent, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
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

    }

}
