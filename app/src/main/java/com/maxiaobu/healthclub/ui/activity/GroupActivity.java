package com.maxiaobu.healthclub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.GroupAdapter;
import com.maxiaobu.healthclub.chat.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanGroupList;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.RequestJsonListener;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2017/1/19.
 * email：maxiaobu1216@gmail.com
 * 功能：群列表
 * 伪码：
 * 待完成：
 */
public class GroupActivity extends BaseAty {
    public static final String TAG = "GroupsActivity";
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    private RecyclerView groupListView;
    protected BeanGroupList grouplist;
    private GroupAdapter groupAdapter;
    private InputMethodManager inputMethodManager;
    public static GroupActivity instance;
    private View progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * 后台的id
     */
    private String groupIid;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    initData();
                    break;
                case 1:
                    Toast.makeText(GroupActivity.this, R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon,mTvTitleCommon,"群列表");
        instance = this;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        groupListView = (RecyclerView) findViewById(R.id.list);
        groupListView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorOrange);
        swipeRefreshLayout.setOnRefreshListener(() -> new Thread() {
            @Override
            public void run() {
                try {
                    // 联网获取群信息
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    handler.sendEmptyMessage(0);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }.start());
        swipeRefreshLayout.setRefreshing(true);
        groupListView.setOnTouchListener((v, event) -> {
            if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                if (getCurrentFocus() != null)
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return false;
        });
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("memid", SPUtils.getString(com.maxiaobu.healthclub.common.Constant.MEMID));
        App.getRequestInstance().post(UrlPath.URL_GROUP_LIST, this, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                swipeRefreshLayout.setRefreshing(false);
                BeanGroupList beanGroupList = new Gson().fromJson(s,BeanGroupList.class);
                if(beanGroupList.getMsgFlag().equals("1")){
                    grouplist = beanGroupList;
                    groupAdapter = new GroupAdapter(GroupActivity.this,grouplist);
                    groupListView.setAdapter(groupAdapter);
                    groupAdapter.setEnterListener(position -> {
                        // enter group chat
                        Intent intent = new Intent(GroupActivity.this, ChatActivity.class);
                        // it is group chat
                        intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                        intent.putExtra("userId", grouplist.getGroupList().get(position).getImid());
                        intent.putExtra("groupId",grouplist.getGroupList().get(position).getGroupid());
                        startActivityForResult(intent, 0);
                    });
                }
            }

            @Override
            public void requestError(VolleyError volleyError, String s) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void noInternet(VolleyError volleyError, String s) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
